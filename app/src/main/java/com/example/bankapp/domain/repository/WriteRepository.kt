package com.example.bankapp.domain.repository

interface WriteRepository<T> {
    suspend fun addDb(entity: T)
}