package com.example.starbucksappclone.repository

import com.example.starbucksappclone.data.local.MemberDao
import com.example.starbucksappclone.data.local.MemberEntity
import com.example.starbucksappclone.data.local.TransactionDao
import com.example.starbucksappclone.data.local.TransactionEntity
import kotlinx.coroutines.flow.Flow

class CoffeeBlissRepository(
    private val memberDao: MemberDao,
    private val transactionDao: TransactionDao
) {

    fun getAllMembers(): Flow<List<MemberEntity>> {
        return memberDao.getAllMembers()
    }

    suspend fun getMemberById(memberId: Int): MemberEntity? {
        return memberDao.getMemberById(memberId)
    }

    suspend fun addMember(member: MemberEntity) {
        memberDao.insertMember(member)
    }

    suspend fun addTransaction(memberId: Int, amount: Double) {
        // Calculate points: 1 Point for every Rp10.000
        val pointsEarned = (amount / 10000).toInt()
        val transaction = TransactionEntity(
            memberId = memberId,
            amount = amount,
            pointEarned = pointsEarned,
            date = System.currentTimeMillis().toString() // simple date representation
        )
        transactionDao.insertTransaction(transaction)

        // Update member's total points
        val currentMember = memberDao.getMemberById(memberId)
        if (currentMember != null) {
            val newPoints = currentMember.points + pointsEarned
            memberDao.updateMemberPoints(memberId, newPoints)
        }
    }

    suspend fun redeemReward(memberId: Int, pointsRequired: Int): Boolean {
        val currentMember = memberDao.getMemberById(memberId)
        if (currentMember != null && currentMember.points >= pointsRequired) {
            val newPoints = currentMember.points - pointsRequired
            memberDao.updateMemberPoints(memberId, newPoints)
            return true
        }
        return false
    }

    fun getTransactionsByMember(memberId: Int): Flow<List<TransactionEntity>> {
        return transactionDao.getTransactionsByMemberId(memberId)
    }
}
