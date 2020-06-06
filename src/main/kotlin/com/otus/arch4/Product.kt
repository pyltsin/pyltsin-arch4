package com.otus.arch4

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonSetter
import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.io.Serializable
import java.util.*

@Table("products")
data class Product(
        @Id
        @Column("id")
        @JsonIgnore
        var idInner: String? = null,
        val name: String,
        val price: Int,
        val description: String) : Persistable<String>, Serializable {

    @JsonIgnore
    override fun isNew(): Boolean {
        return idInner == null
    }


    override fun getId(): String? {
        if (idInner == null) {
            idInner = UUID.randomUUID().toString()
        }
        return idInner
    }

    @JsonSetter
    fun setId(id: String) {
        idInner = id
    }
}