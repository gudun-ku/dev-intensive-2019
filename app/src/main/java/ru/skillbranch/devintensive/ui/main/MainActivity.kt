package ru.skillbranch.devintensive.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.data.UserItem
import ru.skillbranch.devintensive.ui.adapters.ChatAdapter
import ru.skillbranch.devintensive.ui.adapters.ChatItemTouchHelperCallback
import ru.skillbranch.devintensive.ui.base.BaseActivity
import ru.skillbranch.devintensive.ui.group.GroupActivity
import ru.skillbranch.devintensive.viewmodels.MainViewModel

class MainActivity : BaseActivity() {

    private lateinit var chatAdapter: ChatAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)
        initToolbar()
        initViews()
        initViewModel()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
    }

    private fun initViews() {

        chatAdapter = ChatAdapter{
            val snackbar = Snackbar.make(rv_chat_list, "Кликнули на ${it.title}", Snackbar.LENGTH_LONG)
            snackbar.view.background = resources.getDrawable(R.drawable.bg_snackbar, theme)
            snackbar.setAction("ОТМЕНА") {
                snackbar.dismiss()
            }
            snackbar.show()
        }


        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        val touchCallBack = ChatItemTouchHelperCallback(chatAdapter) { chatItem ->
            viewModel.addToArchive(chatItem.id)
            val snackbar = Snackbar.make(rv_chat_list, "Вы точно хотите добавить ${chatItem.title} в архив?", Snackbar.LENGTH_LONG)
            snackbar.view.background = resources.getDrawable(R.drawable.bg_snackbar, theme)
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


    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.getChatData().observe(this, Observer { chatAdapter.updateData(it)})
    }

}
