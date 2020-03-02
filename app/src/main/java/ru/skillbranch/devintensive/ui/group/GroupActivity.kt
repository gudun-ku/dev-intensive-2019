package ru.skillbranch.devintensive.ui.group

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.children
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_group.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.extensions.colorFromAttribute
import ru.skillbranch.devintensive.models.data.UserItem
import ru.skillbranch.devintensive.ui.adapters.UserAdapter
import ru.skillbranch.devintensive.viewmodels.GroupViewModel
import ru.skillbranch.devintensive.viewmodels.ProfileViewModel


class GroupActivity : AppCompatActivity() {

    private lateinit var usersAdapter: UserAdapter
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var viewModel: GroupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)
        setTheme(R.style.AppTheme)
        initToolbar()
        initViews()
        initViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.queryHint = "Введите имя пользователя"

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
        supportActionBar?.title = "Создание группы"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initViews() {
        usersAdapter = UserAdapter {viewModel.handleSelectedItem(it.id)}
        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        with(rv_user_list) {
            adapter = usersAdapter
            layoutManager = LinearLayoutManager(this@GroupActivity)
            addItemDecoration(divider)
        }

        fab.setOnClickListener {
            viewModel.handleCreateGroup()
            finish()
            overridePendingTransition(R.anim.idle, R.anim.bottom_down)
        }
    }

    private fun initViewModel() {
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        profileViewModel.getTheme().observe(this, Observer { updateTheme(it) })
        viewModel = ViewModelProviders.of(this).get(GroupViewModel::class.java)
        viewModel.getUsersData().observe(this, Observer { usersAdapter.updateData(it)})
        viewModel.getSelectedData().observe(this, Observer {
            updateChips(it)
            toggleFab(it.size > 1)
        })
    }

    private fun toggleFab(isShown: Boolean) {
        if (isShown) fab.show()
        else fab.hide()
    }

    private fun addChipToGroup(user: UserItem) {

        val chip = getChipWithIcon(user, resources.getDrawable(R.drawable.avatar_default, theme))
        chip.setOnCloseIconClickListener{ viewModel.handleRemoveChip(it.tag.toString())}
        chip_group.addView(chip)
    }

    private fun updateChips(listUsers: List<UserItem>) {

        chip_group.visibility = if (listUsers.isEmpty()) View.GONE else View.VISIBLE

        val users = listUsers
            .associate { user -> user.id to user }
            .toMutableMap()

        val views = chip_group.children
            .associate { view -> view.tag to view}

        for ((k,v) in views ) {
            if(!users.containsKey(k)) chip_group.removeView(v)
            else users.remove(k)
        }

        users.forEach{(_,v)-> addChipToGroup(v) }
    }

    private fun updateTheme(mode: Int) {
        delegate.setLocalNightMode(mode)
    }

    fun getChipWithIcon(user: UserItem, errDrawable: Drawable):Chip {
        val chip = Chip(this).apply {
            text = user.fullName
            chipIcon = errDrawable
            isCloseIconVisible = true
            tag = user.id
            isClickable = true
            closeIconTint = ColorStateList.valueOf(this.context.colorFromAttribute(R.attr.colorChipIconBackground))
            chipBackgroundColor = ColorStateList.valueOf(this.context.colorFromAttribute(R.attr.colorAccentedSurface))
            setTextColor(Color.WHITE)
        }
        /*
          Glide.with(this)
                .load(user.avatar)
                .listener(object : RequestListener<Drawable?> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable?>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        chip.chipIcon = errDrawable
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable?>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        chip.chipIcon = resource
                        return false
                    }
                }).preload()
         */
       return chip
    }

}
