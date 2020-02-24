package com.teltronic.app112.classes

import com.teltronic.app112.R

enum class Category(val idTitle: Int, val idIcon: Int) {
    CRIME(R.string.txt_crime, R.drawable.ic_police_car_white),
    ACCIDENT(R.string.txt_accident, R.drawable.ic_accident_white),
    MEDICAL_URGENCY(R.string.txt_medical_urgency, R.drawable.ic_hospital_white),
    OTHER(R.string.txt_other, R.drawable.ic_more_white)
}