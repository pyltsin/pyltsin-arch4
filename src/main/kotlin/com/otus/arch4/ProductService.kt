package com.otus.arch4

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.util.*
import javax.annotation.PostConstruct
import kotlin.random.Random

private val random = Random(1)

@Service
class ProductService {

    @Autowired
    lateinit var productRepo: ProductRepo

    @Cacheable(value = ["default"])
    fun findAllByPriceBetweenAndAndDescriptionLike(maxPrice: Int, minPrice: Int, description: String): List<Product> {
        logger().info("miss")
        return productRepo.findAllByPriceBetweenAndAndDescriptionLike(maxPrice, minPrice, description)
    }

    @PostConstruct
    fun init() {
        productRepo.deleteAll()
        for (i in 0 until 1000) {
            productRepo.save(generateProduct())
        }
    }

    private fun generateProduct(): Product {
        return Product(
                name = UUID.randomUUID().toString(),
                price = random.nextInt(0, 100),
                description = UUID.randomUUID().toString()
        )
    }
}

fun <T : Any> T.logger(): Logger = getLogger(javaClass)

fun getLogger(forClass: Class<*>): Logger = LoggerFactory.getLogger(forClass)