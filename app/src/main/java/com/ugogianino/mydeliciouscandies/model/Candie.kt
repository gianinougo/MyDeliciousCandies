package com.ugogianino.mydeliciouscandies.model

data class Candie(
    var id: Int,
    var name: String = "",
    var manufacturer: Manufacturer,
    var candyType: CandyType,
    var format: Format,
    var sweetness: Int,
    var image: ByteArray?,
    var url: String = "",
    var isFavourite: Boolean = false
)

