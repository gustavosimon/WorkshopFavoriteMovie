package com.jessica.yourfavoritemovies.favorites.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jessica.yourfavoritemovies.Constants
import com.jessica.yourfavoritemovies.MovieUtil
import com.jessica.yourfavoritemovies.model.Result

class FavoriteViewModel(application: Application): AndroidViewModel(application) {
    var stateRemoveFavorite: MutableLiveData<Result> = MutableLiveData()
    var stateList: MutableLiveData<List<Result>> = MutableLiveData()

    fun getFavorites() {
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference(MovieUtil.getUserId(getApplication()).toString() + Constants.FAVORITES_PATH)

        reference.orderByKey().addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val favorites = mutableListOf<Result>()
                for (resultSnapshot in snapshot.children) {
                    val result = resultSnapshot.getValue(Result::class.java)
                result?.let { favorites.add(it) }
            }
                stateList.value = favorites
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun removeFavoriteClickListener(result: Result) {
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference(MovieUtil.getUserId(getApplication()).toString() + Constants.FAVORITES_PATH)
        reference.orderByChild(Constants.ID_PATH).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (resultSnapshot in snapshot.children) {
                    val resultFirebase: Result? = resultSnapshot.getValue(Result::class.java)
                    if (result.id == resultFirebase?.id) {
                        resultSnapshot.ref.removeValue()
                        stateRemoveFavorite.value = result
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })

    }
}