package ru.spb.isamokhvalov.jmh

import org.openjdk.jmh.annotations.*
import org.openjdk.jmh.infra.Blackhole
import java.util.*
import java.util.concurrent.TimeUnit

@State(Scope.Thread)
@Warmup(time = 1, timeUnit = TimeUnit.SECONDS, iterations = 3)
@Measurement(time = 1, timeUnit = TimeUnit.SECONDS)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
open class PredicateComparableTest {



    @Param("10","100","1000")
    private var size: Int = 0
    lateinit var products: List<Int>


    @Setup
    fun setupCollection() {
        val random = Random()
        val result = mutableListOf<Int>()
        IntRange(1, size).onEach {
            result += random.nextInt()
        }
        products = result.toList()
    }


    @Benchmark
    fun testStupid(blackhole: Blackhole) {
        val result = products
            .filter { it != 10 }
            .filter { it != 11 }
            .filter { it != 20 }
            .filter { it != 30 }
            .filter { it != 40 }
        blackhole.consume(result.size)
    }

    @Benchmark
    fun testUnion(blackhole: Blackhole) {
        val result = products.filter {
            it != 10 &&
                    it != 11 &&
                    it != 20 &&
                    it != 30 &&
                    it != 40
        }
        blackhole.consume(result.size)
    }

    @Benchmark
    fun testInSet(blackhole: Blackhole) {

        val dataSet = setOf(10, 11, 20, 30, 40)
        val result = products.filter { it !in dataSet }
        blackhole.consume(result.size)
    }

    @Benchmark
    fun testBuildPredicate(blackhole: Blackhole) {
        val commonPredicate = fun(it: Int): Boolean {
            return it != 10 &&
                    it != 11 &&
                    it != 20 &&
                    it != 30 &&
                    it != 40
        }

        val result = products.filter { commonPredicate(it) }
        blackhole.consume(result.size)
    }

    @Benchmark
    fun testSequence(blackhole: Blackhole) {
        val result = products
            .asSequence()
            .filter { it != 10 }
            .filter { it != 11 }
            .filter { it != 20 }
            .filter { it != 30 }
            .filter { it != 40 }
            .toList()
        blackhole.consume(result.size)
    }

}


//REMEMBER: The numbers below are just data. To gain reusable insights, you need to follow up on
//why the numbers are the way they are. Use profilers (see -prof, -lprof), design factorial
//experiments, perform baseline and negative tests that provide experimental control, make sure
//the benchmarking environment is safe on JVM/OS/HW level, ask for reviews from the domain experts.
//Do not assume the numbers tell you what you want them to tell.
//
//Benchmark                                   Mode  Cnt      Score      Error  Units
//PredicateComparableTest.testBuildPredicate  avgt   25  11155,198 ±  129,609  ns/op
//PredicateComparableTest.testSequence        avgt   25  51652,939 ±  818,110  ns/op
//PredicateComparableTest.testStupid          avgt   25  43185,824 ± 2836,390  ns/op
//PredicateComparableTest.testUnion           avgt   25  10263,423 ±  634,664  ns/op


//REMEMBER: The numbers below are just data. To gain reusable insights, you need to follow up on
//why the numbers are the way they are. Use profilers (see -prof, -lprof), design factorial
//experiments, perform baseline and negative tests that provide experimental control, make sure
//the benchmarking environment is safe on JVM/OS/HW level, ask for reviews from the domain experts.
//Do not assume the numbers tell you what you want them to tell.
//
//Benchmark                                   Mode  Cnt      Score      Error  Units
//PredicateComparableTest.testBuildPredicate  avgt   25  11157,247 ±  112,359  ns/op
//PredicateComparableTest.testInSet           avgt   25  13309,015 ±  304,587  ns/op
//PredicateComparableTest.testSequence        avgt   25  50923,758 ±  328,278  ns/op
//PredicateComparableTest.testStupid          avgt   25  43597,486 ± 1222,480  ns/op
//PredicateComparableTest.testUnion           avgt   25   9263,547 ±  201,701  ns/op

