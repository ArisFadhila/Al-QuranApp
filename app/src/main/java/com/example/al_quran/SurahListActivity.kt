package com.example.al_quran

import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.al_quran.adapter.SurahAdapter
import com.example.al_quran.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SurahListActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SurahAdapter
    private lateinit var allSurahList: List<Surah>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_surah_list)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = SurahAdapter { surah -> openSurahDetails(surah) }
        recyclerView.adapter = adapter

        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterSurahList(newText.orEmpty())
                return true
            }
        })

        fetchSurahList()
    }

    private fun fetchSurahList() {
        RetrofitClient.instance.getSurahList().enqueue(object : Callback<SurahListResponse> {
            override fun onResponse(call: Call<SurahListResponse>, response: Response<SurahListResponse>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    data?.data?.let {
                        allSurahList = it // simpan semua data
                        adapter.submitList(it)
                    }
                } else {
                    Toast.makeText(this@SurahListActivity, "Gagal memuat data (response gagal)", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SurahListResponse>, t: Throwable) {
                Toast.makeText(this@SurahListActivity, "Gagal memuat data (jaringan)", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun filterSurahList(query: String) {
        val filtered = allSurahList.filter {
            it.englishName.contains(query, ignoreCase = true) ||
                    it.name.contains(query, ignoreCase = true)
        }
        adapter.submitList(filtered)
    }

    private fun openSurahDetails(surah: Surah) {
        val intent = Intent(this, SurahDetailsActivity::class.java)
        intent.putExtra("SURAH_ID", surah.number)
        intent.putExtra("SURAH_NAME", surah.englishName)
        intent.putExtra("SURAH_TYPE", surah.revelationType)
        startActivity(intent)
    }
}