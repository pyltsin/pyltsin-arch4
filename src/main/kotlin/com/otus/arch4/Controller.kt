package com.otus.arch4

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.annotation.PostConstruct
import kotlin.random.Random


@RestController
@RequestMapping("/products")
class Controller {
    @Autowired
    private lateinit var productService: ProductService

    @PostMapping
    fun find(@RequestBody searchQuery: SearchQuery): List<Product> {


        return productService.findAllByPriceBetweenAndAndDescriptionLike(
                maxPrice = searchQuery.maxPrice,
                minPrice = searchQuery.minPrice,
                description = "%" + searchQuery.description + "%"
        )
    }

}

data class SearchQuery(val maxPrice: Int, val minPrice: Int, val description: String)


