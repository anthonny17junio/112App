package com.teltronic.app112.classes.enums

enum class DistanceValues(val id:Int, val valueKm:Int) {
    NONE_KM(0,0),
    ONE_KM(1,1),
    FIVE_KM(2,5),
    TEN_KM(3,10),
    THIRTY_KM(4,30),
    FIFTY_KM(5,50),
    ONE_HUNDRED_KM(6,100),
    NO_LIMIT(7,-1);

    companion object {
        fun getById(id: Int): DistanceValues? {
            for (distance in values()) {
                if (distance.id == id) {
                    return distance
                }
            }
            return null
        }
    }
}