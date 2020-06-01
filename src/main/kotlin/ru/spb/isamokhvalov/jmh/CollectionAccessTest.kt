package ru.spb.isamokhvalov.jmh

import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import java.util.*
import java.util.concurrent.TimeUnit

@State(Scope.Thread)
@Warmup(time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(time = 1, timeUnit = TimeUnit.SECONDS)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
open class CollectionAccessTest {
    val size: Int = 1000

    lateinit var listCollection: List<Long>
    lateinit var setCollection: Set<Long>

    lateinit var indexes: Collection<Long>


    @Setup
    fun setupCollection() {
        val random = Random()
        val result = mutableListOf<Long>()
        IntRange(1, size).onEach {
            result += random.nextLong()
        }
        indexes = listOf(result.first(), result.last(), result[10], result[100], result[900])
        listCollection = result.toList()
        setCollection = result.toSet()
    }

    @Benchmark
    fun testList(blackHole: Blackhole) {
        indexes.forEach {
            blackHole.consume(listCollection.contains(it))

        }
    }

    @Benchmark
    fun testSet(blackHole: Blackhole) {
        indexes.forEach {
            blackHole.consume(setCollection.contains(it))

        }
    }


    private fun process(testableCollection: Collection<Long>, blackHole: Blackhole) {
        indexes.forEach {
            blackHole.consume(testableCollection.contains(it))

        }

    }
}