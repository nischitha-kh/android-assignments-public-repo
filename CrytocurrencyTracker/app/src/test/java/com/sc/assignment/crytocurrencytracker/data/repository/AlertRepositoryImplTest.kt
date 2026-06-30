package com.sc.assignment.crytocurrencytracker.data.repository

import com.sc.assignment.crytocurrencytracker.data.local.AlertDao
import com.sc.assignment.crytocurrencytracker.data.local.entity.AlertEntity
import com.sc.assignment.crytocurrencytracker.domain.model.Alert
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class AlertRepositoryImplTest {

    private val dao = mockk<AlertDao>()
    private val repository = AlertRepositoryImpl(dao)

    @Test
    fun `addAlert maps and calls dao`() = runBlocking {
        val alert = Alert(coinId = "bitcoin", coinSymbol = "BTC", targetPrice = 60000.0, isAbove = true)
        coEvery { dao.insertAlert(any()) } returns Unit

        repository.addAlert(alert)

        coVerify { dao.insertAlert(match { it.coinId == "bitcoin" }) }
    }

    @Test
    fun `toggleAlert calls dao`() = runBlocking {
        coEvery { dao.updateAlertStatus(any(), any()) } returns Unit
        
        repository.toggleAlert(1, false)
        
        coVerify { dao.updateAlertStatus(1, false) }
    }
}
