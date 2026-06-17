package com.example.starbucksappclone.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.starbucksappclone.data.local.MemberEntity
import com.example.starbucksappclone.data.local.TransactionEntity
import com.example.starbucksappclone.repository.CoffeeBlissRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CoffeeBlissViewModel(private val repository: CoffeeBlissRepository) : ViewModel() {

    private val _members = MutableStateFlow<List<MemberEntity>>(emptyList())
    val members: StateFlow<List<MemberEntity>> = _members.asStateFlow()

    private val _currentMember = MutableStateFlow<MemberEntity?>(null)
    val currentMember: StateFlow<MemberEntity?> = _currentMember.asStateFlow()

    private val _memberTransactions = MutableStateFlow<List<TransactionEntity>>(emptyList())
    val memberTransactions: StateFlow<List<TransactionEntity>> = _memberTransactions.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllMembers().collect {
                _members.value = it
            }
        }
    }

    fun loadMember(memberId: Int) {
        viewModelScope.launch {
            _currentMember.value = repository.getMemberById(memberId)
            repository.getTransactionsByMember(memberId).collect {
                _memberTransactions.value = it
            }
        }
    }

    fun addMember(name: String, email: String, phone: String, onComplete: (Int) -> Unit) {
        viewModelScope.launch {
            repository.addMember(MemberEntity(name = name, email = email, phone = phone))
            val list = repository.getAllMembers().first()
            val newMember = list.find { it.email == email }
            if (newMember != null) {
                _currentMember.value = newMember
                onComplete(newMember.id)
            }
        }
    }

    fun login(email: String, onResult: (Boolean, Int?) -> Unit) {
        viewModelScope.launch {
            val list = repository.getAllMembers().first()
            val member = list.find { it.email == email }
            if (member != null) {
                _currentMember.value = member
                onResult(true, member.id)
            } else {
                onResult(false, null)
            }
        }
    }

    fun addTransaction(memberId: Int, amount: Double, onComplete: (Int, Int) -> Unit) {
        viewModelScope.launch {
            repository.addTransaction(memberId, amount)
            val updatedMember = repository.getMemberById(memberId)
            _currentMember.value = updatedMember
            if (updatedMember != null) {
                val pointsEarned = (amount / 10000).toInt()
                onComplete(pointsEarned, updatedMember.points)
            }
        }
    }

    fun redeemReward(memberId: Int, pointsRequired: Int, onSuccess: () -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            val success = repository.redeemReward(memberId, pointsRequired)
            if (success) {
                _currentMember.value = repository.getMemberById(memberId)
                onSuccess()
            } else {
                onError()
            }
        }
    }
}

class CoffeeBlissViewModelFactory(private val repository: CoffeeBlissRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CoffeeBlissViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CoffeeBlissViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
