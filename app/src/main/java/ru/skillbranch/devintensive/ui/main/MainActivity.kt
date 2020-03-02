package ru.skillbranch.devintensive.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.data.ChatType
import ru.skillbranch.devintensive.ui.adapters.ChatAdapter
import ru.skillbranch.devintensive.ui.adapters.ChatItemTouchHelperCallback
import ru.skillbranch.devintensive.ui.archive.ArchiveActivity
import ru.skillbranch.devintensive.ui.group.GroupActivity
import ru.skillbranch.devintensive.viewmodels.MainViewModel
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ru.skillbranch.devintensive.extensions.colorFromAttribute
import ru.skillbranch.devintensive.ui.profile.ProfileActivity
import ru.skillbranch.devintensive.viewmodels.ProfileViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var chatAdapter: ChatAdapter
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTheme(R.style.AppTheme)
        initToolbar()
        initViews()
        initViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.queryHint = "Введите заголовок чата"

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.handleSearchQuery(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.handleSearchQuery(newText)
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp)
    }

    private fun initViews() {

        chatAdapter = ChatAdapter{
            when(it.chatType) {

                ChatType.ARCHIVE -> {
                    val intent = Intent(this, ArchiveActivity::class.java)
                    startActivity(intent)
                 }
                else -> {
                    val snackbar = Snackbar.make(rv_chat_list, "Кликнули на ${it.title}", Snackbar.LENGTH_LONG)
                    snackbar.view.background = resources.getDrawable(R.drawable.bg_snackbar, theme)
                    val tv = snackbar.view.findViewById(R.id.snackbar_text) as TextView
                    tv.setTextColor(colorFromAttribute(R.attr.colorSnackbarText))
                    snackbar.setAction("ОТМЕНА") {
                        snackbar.dismiss()
                    }
                    snackbar.show()
                }

            }

        }


        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        val touchCallBack = ChatItemTouchHelperCallback(chatAdapter, R.drawable.ic_archive_black_24dp) { chatItem ->
            viewModel.addToArchive(chatItem.id)
            val snackbar = Snackbar.make(rv_chat_list, "Вы точно хотите добавить ${chatItem.title} в архив?", Snackbar.LENGTH_LONG)
            snackbar.view.background = resources.getDrawable(R.drawable.bg_snackbar, theme)
            val tv = snackbar.view.findViewById(R.id.snackbar_text) as TextView
            tv.setTextColor(colorFromAttribute(R.attr.colorSnackbarText))

            snackbar.setAction("ОТМЕНА") {
                viewModel.restoreFromArchive(chatItem.id)
                snackbar.dismiss()
            }
            snackbar.show()
        }

        val touchHelper = ItemTouchHelper(touchCallBack)
        touchHelper.attachToRecyclerView(rv_chat_list)

        with(rv_chat_list) {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            addItemDecoration(divider)
        }

        fab.setOnClickListener {
            val intent = Intent(this, GroupActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if(item?.itemId == android.R.id.home) {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun initViewModel() {
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        profileViewModel.getTheme().observe(this, Observer { updateTheme(it) })
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.getChatData().observe(this, Observer { chatAdapter.updateData(it)})
    }

    private fun updateTheme(mode: Int) {
        delegate.setLocalNightMode(mode)
    }

}
