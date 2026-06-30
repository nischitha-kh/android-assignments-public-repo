package com.sc.assignment.crytocurrencytracker.worker

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.sc.assignment.crytocurrencytracker.domain.repository.AlertRepository
import com.sc.assignment.crytocurrencytracker.domain.repository.CoinRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SyncWorkerTest {

    private lateinit var context: Context
    private val coinRepo = mockk<CoinRepository>()
    private val alertRepo = mockk<AlertRepository>()

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun `doWork returns success when refresh completes`() = runBlocking {
        coEvery { coinRepo.refreshCoins() } returns Unit
        coEvery { alertRepo.getEnabledAlerts() } returns emptyList()

        val worker = SyncWorker(context, mockk(relaxed = true), coinRepo, alertRepo)
        val result = worker.doWork()

        assertEquals(ListenableWorker.Result.success(), result)
    }

    @Test
    fun `doWork returns retry when refresh fails`() = runBlocking {
        coEvery { coinRepo.refreshCoins() } throws Exception("Network error")

        val worker = SyncWorker(context, mockk(relaxed = true), coinRepo, alertRepo)
        val result = worker.doWork()

        assertEquals(ListenableWorker.Result.retry(), result)
    }
}
