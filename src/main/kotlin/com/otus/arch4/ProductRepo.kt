package com.otus.arch4

import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Transactional
@Repository
interface ProductRepo : CrudRepository<Product, String> {
    @Query("Select * from products where price<:maxPrice and price>:minPrice and description like :description")
    fun findAllByPriceBetweenAndAndDescriptionLike(maxPrice: Int, minPrice: Int, description: String): List<Product>
}
