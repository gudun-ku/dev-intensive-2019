package ru.skillbranch.devintensive.ui.archive

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
import kotlinx.android.synthetic.main.activity_archive.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.toolbar
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.ui.adapters.ChatAdapter
import ru.skillbranch.devintensive.ui.adapters.ChatItemTouchHelperCallback
import ru.skillbranch.devintensive.ui.base.BaseActivity
import ru.skillbranch.devintensive.viewmodels.ArchiveViewModel



class ArchiveActivity : BaseActivity() {

    private lateinit var chatAdapter: ChatAdapter
    private lateinit var viewModel: ArchiveViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archive)
        initToolbar()
        initViews()
        initViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.queryHint = "Введите название чата"

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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item?.itemId == android.R.id.home) {
            finish()
            overridePendingTransition(R.anim.idle, R.anim.bottom_down)
            true
        } else super.onOptionsItemSelected(item)
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Архив чатов"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initViews() {

        chatAdapter = ChatAdapter {
            val snackbar = Snackbar.make(rv_archive_list, "Кликнули на ${it.title}", Snackbar.LENGTH_LONG)
            snackbar.view.background = resources.getDrawable(R.drawable.bg_snackbar, theme)
            snackbar.setAction("ОТМЕНА") {
                snackbar.dismiss()
            }
            snackbar.show()
        }

        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        val touchCallBack = ChatItemTouchHelperCallback(chatAdapter) { chatItem ->
            viewModel.restoreFromArchive(chatItem.id)
            val snackbar = Snackbar.make(
                rv_archive_list,
                "Вы точно хотите восстановить ${chatItem.title} из архива?",
                Snackbar.LENGTH_LONG
            )
            snackbar.view.background = resources.getDrawable(R.drawable.bg_snackbar, theme)
            snackbar.setAction("ОТМЕНА") {
                viewModel.addToArchive(chatItem.id)
                snackbar.dismiss()
            }
            snackbar.show()
        }

        val touchHelper = ItemTouchHelper(touchCallBack)
        touchHelper.attachToRecyclerView(rv_archive_list)

        with(rv_archive_list) {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(this@ArchiveActivity)
            addItemDecoration(divider)
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ArchiveViewModel::class.java)
        viewModel.getChatData().observe(this, Observer { chatAdapter.updateData(it)})
    }

}
