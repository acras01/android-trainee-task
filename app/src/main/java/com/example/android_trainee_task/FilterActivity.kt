package com.example.android_trainee_task

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.android_trainee_task.databinding.ActivityFilterBinding


class FilterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFilterBinding

    private var serviceItems = arrayListOf<ServiceItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        serviceItems = intent?.extras?.getParcelableArrayList("services")!!
        val serviceListView: ListView = findViewById(R.id.servicesList)
        val services = arrayListOf<String>()
        serviceItems.indices.forEach { i ->
            services.add(serviceItems[i].name!!)
        }
        val adapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, services)

        serviceListView.adapter = adapter
        serviceListView.choiceMode = ListView.CHOICE_MODE_MULTIPLE
        serviceItems.indices.forEach { i ->
            serviceListView.setItemChecked(i, serviceItems[i].checked)
        }

        val itemClickListener =
            OnItemClickListener { arg0, _, position, _ ->
                val lv = arg0 as ListView
                val serviceItem: ServiceItem = serviceItems[position]
                serviceItem.checked = lv.isItemChecked(position)
            }
        serviceListView.onItemClickListener = itemClickListener
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            val data = Intent().apply {
                putParcelableArrayListExtra("services", serviceItems)
            }
            setResult(Activity.RESULT_OK, data)
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}