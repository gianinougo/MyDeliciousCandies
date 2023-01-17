package com.ugogianino.mydeliciouscandies.model

data class Candie(
    var id: Int,
    var name: String = "",
    var manufacturer: String = "",
    var candyType: String = "",
    var saleFormat: String = "",
    var sweetness: Int,
    var image: ByteArray,
    var url: String = "",
    var isFavourite: Boolean = false,

) {

}

