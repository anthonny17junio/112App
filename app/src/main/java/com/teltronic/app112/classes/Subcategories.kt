package com.teltronic.app112.classes

import com.teltronic.app112.classes.enums.Category
import com.teltronic.app112.classes.enums.Subcategory

class Subcategories(category: Category) {

    var listSubcategories: List<Subcategory>? = when (category) {
        Category.CRIME ->
            getCrimeSubcategories()
        Category.ACCIDENT ->
            getAccidentSubcategories()
        Category.MEDICAL_URGENCY ->
            getMedicalUrgencySubcategories()
        else ->
            null
    }

}

private fun getCrimeSubcategories(): List<Subcategory> {
    return listOf(
        Subcategory.CRIME_ROBBERY,
        Subcategory.CRIME_VANDALISM,
        Subcategory.CRIME_AGGRESSION,
        Subcategory.CRIME_SEXUAL_ASSAULT,
        Subcategory.CRIME_GENDER_VIOLENCE,
        Subcategory.CRIME_BULLYING,
        Subcategory.CRIME_RADICALISMS
    )

}

private fun getAccidentSubcategories(): List<Subcategory> {
    return listOf(
        Subcategory.ACCIDENT_CAR_WITH_INJURED,
        Subcategory.ACCIDENT_CAR_WITHOUT_INJURED,
        Subcategory.ACCIDENT_VEHICLE_FIRE,
        Subcategory.ACCIDENT_STREET_ACCIDENT,
        Subcategory.ACCIDENT_HOUSING_ACCIDENT,
        Subcategory.ACCIDENT_HOUSE_FIRE,
        Subcategory.ACCIDENT_LOCKED_INSIDE_HOUSE,
        Subcategory.ACCIDENT_LOCKED_INSIDE_ELEVATOR,
        Subcategory.ACCIDENT_MOUNTAIN_ACCIDENT,
        Subcategory.ACCIDENT_MOUNTAIN_LOST,
        Subcategory.ACCIDENT_AQUATIC_RESCUE,
        Subcategory.ACCIDENT_ANIMAL_ATTACK,
        Subcategory.ACCIDENT_OTHER
    )
}

private fun getMedicalUrgencySubcategories(): List<Subcategory> {
    return listOf(
        Subcategory.MEDICAL_URGENCY_FAINTING,
        Subcategory.MEDICAL_URGENCY_HEART_ATTACK,
        Subcategory.MEDICAL_URGENCY_PARALYSIS,
        Subcategory.MEDICAL_URGENCY_DIFFICULTY_BREATHING,
        Subcategory.MEDICAL_URGENCY_CHOCKING,
        Subcategory.MEDICAL_URGENCY_SEIZURES,
        Subcategory.MEDICAL_URGENCY_FRACTURE,
        Subcategory.MEDICAL_URGENCY_HEMORRHAGE,
        Subcategory.MEDICAL_URGENCY_POISONING,
        Subcategory.MEDICAL_URGENCY_IMMINENT_CHILDBIRTH,
        Subcategory.MEDICAL_URGENCY_ELECTROCUTION,
        Subcategory.MEDICAL_URGENCY_BURN,
        Subcategory.MEDICAL_URGENCY_ANIMAL_ATTACK
    )
}