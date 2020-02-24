package com.teltronic.app112.classes

import com.teltronic.app112.R

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
    val listSubcategories: List<Subcategory>

    val subcat1 = Subcategory(
        R.string.subcategory_crime_robbery,
        R.drawable.ic_subcategory_crime_robbery
    )  //Robo, atraco, asalto
    val subcat2 = Subcategory(
        R.string.subcategory_crime_vandalism,
        R.drawable.ic_subcategory_crime_vandalism
    ) //Vandalismo, daño
    val subcat3 = Subcategory(
        R.string.subcategory_crime_aggression,
        R.drawable.ic_subcategory_crime_agression
    ) //Agresión, pelea
    val subcat4 = Subcategory(
        R.string.subcategory_crime_sexual_assault,
        R.drawable.ic_subcategory_crime_sexual_assault
    ) //Agresión sexual
    val subcat5 = Subcategory(
        R.string.subcategory_crime_gender_violence,
        R.drawable.ic_subcategory_crime_gender_violence
    ) //Violencia de género
    val subcat6 = Subcategory(
        R.string.subcategory_crime_bullying,
        R.drawable.ic_subcategory_crime_bullying
    ) //Acoso escolar
    val subcat7 = Subcategory(
        R.string.subcategory_crime_radicalisms,
        R.drawable.ic_subcategory_crime_radicalisms
    ) //Radicalismos
    listSubcategories = listOf(subcat1, subcat2, subcat3, subcat4, subcat5, subcat6, subcat7)

    return listSubcategories
}

private fun getAccidentSubcategories(): List<Subcategory> {
    val listSubcategories: List<Subcategory>

    val subcat1 = Subcategory(
        R.string.subcategory_accident_car_with_injured,
        R.drawable.ic_subcategory_accident_car_with_injured
    ) //Accidente de tráfico con heridos
    val subcat2 = Subcategory(
        R.string.subcategory_accident_car_without_injured,
        R.drawable.ic_subcategory_accident_car_without_injured
    ) //Accidente de tráfico sin heridos
    val subcat3 = Subcategory(
        R.string.subcategory_accident_vehicle_fire,
        R.drawable.ic_subcategory_accident_vehicle_fire
    ) //Incendio de vehículo
    val subcat4 =
        Subcategory(
            R.string.subcategory_accident_street_accident,
            R.drawable.ic_subcategory_accident_street_accident
        ) //Accidente en la calle
    val subcat5 =
        Subcategory(
            R.string.subcategory_accident_housing_accident,
            R.drawable.ic_subcategory_accident_housing_accident
        ) //Accidente en la vivienda
    val subcat6 =
        Subcategory(
            R.string.subcategory_accident_house_fire,
            R.drawable.ic_subcategory_accident_house_fire
        ) //Incendio en la vivienda/edificio
    val subcat7 =
        Subcategory(
            R.string.subcategory_accident_locked_inside_house,
            R.drawable.ic_subcategory_accident_locked_inside_house
        ) //Encerrado en la vivienda
    val subcat8 = Subcategory(
        R.string.subcategory_accident_locked_inside_elevator,
        R.drawable.ic_subcategory_accident_locked_inside_elevator
    ) //Encerrado en el ascensor
    val subcat9 = Subcategory(
        R.string.subcategory_accident_mountain_accident,
        R.drawable.ic_subcategory_accident_mountain_accident
    ) //Accidente / Rescate en el monte
    val subcat10 =
        Subcategory(
            R.string.subcategory_accident_mountain_lost,
            R.drawable.ic_subcategory_accident_mountain_lost
        ) //Extraviado en el monte
    val subcat11 =
        Subcategory(
            R.string.subcategory_accident_aquatic_rescue,
            R.drawable.ic_subcategory_accident_aquatic_rescue
        ) //Salvamento / rescate acuático
    val subcat12 = Subcategory(
        R.string.subcategory_accident_animal_attack,
        R.drawable.ic_subcategory_accident_animal_attack
    ) //Ataque de animal
    val subcat13 = Subcategory(
        R.string.subcategory_accident_other,
        R.drawable.ic_subcategory_accident_other
    ) //Otros

    listSubcategories = listOf(
        subcat1,
        subcat2,
        subcat3,
        subcat4,
        subcat5,
        subcat6,
        subcat7,
        subcat8,
        subcat9,
        subcat10,
        subcat11,
        subcat12,
        subcat13
    )

    return listSubcategories
}

private fun getMedicalUrgencySubcategories(): List<Subcategory> {
    val listSubcategories: List<Subcategory>

    val subcat1 = Subcategory(
        R.string.subcategory_medical_urgency_fainting,
        R.drawable.ic_subcategory_medical_urgency_fainting
    ) //Pérdida de conocimiento / desmayo
    val subcat2 = Subcategory(
        R.string.subcategory_medical_urgency_heart_attack,
        R.drawable.ic_subcategory_medical_urgency_heart_attack
    ) //Dolor de pecho / infarto
    val subcat3 = Subcategory(
        R.string.subcategory_medical_urgency_paralysis,
        R.drawable.ic_subcategory_medical_urgency_paralysis
    ) //Parálisis
    val subcat4 = Subcategory(
        R.string.subcategory_medical_urgency_difficulty_breathing,
        R.drawable.ic_subcategory_medical_urgency_difficulty_breathing
    ) //Dificultad al respirar
    val subcat5 = Subcategory(
        R.string.subcategory_medical_urgency_chocking,
        R.drawable.ic_subcategory_medical_urgency_chocking
    ) //Atragantamiento
    val subcat6 = Subcategory(
        R.string.subcategory_medical_urgency_seizures,
        R.drawable.ic_subcategory_medical_urgency_seizures
    ) //Convulsiones
    val subcat7 = Subcategory(
        R.string.subcategory_medical_urgency_fracture,
        R.drawable.ic_subcategory_medical_urgency_fracture
    ) //Fractura / traumatismo
    val subcat8 = Subcategory(
        R.string.subcategory_medical_urgency_hemorrhage,
        R.drawable.ic_subcategory_medical_urgency_hemorrhage
    ) //Hemorragia
    val subcat9 = Subcategory(
        R.string.subcategory_medical_urgency_poisoning,
        R.drawable.ic_subcategory_medical_urgency_poisoning
    ) //Intoxicación
    val subcat10 = Subcategory(
        R.string.subcategory_medical_urgency_imminent_childbirth,
        R.drawable.ic_subcategory_medical_urgency_imminent_childbirth
    ) //Parto inminente
    val subcat11 = Subcategory(
        R.string.subcategory_medical_urgency_electrocution,
        R.drawable.ic_subcategory_medical_urgency_electrocution
    ) //Electrocución
    val subcat12 = Subcategory(
        R.string.subcategory_medical_urgency_burn,
        R.drawable.ic_subcategory_medical_urgency_burn
    ) //Quemadura
    val subcat13 = Subcategory(
        R.string.subcategory_medical_urgency_animal_attack,
        R.drawable.ic_subcategory_medical_urgency_animal_attack
    ) //Ataque de animal

    listSubcategories = listOf(
        subcat1,
        subcat2,
        subcat3,
        subcat4,
        subcat5,
        subcat6,
        subcat7,
        subcat8,
        subcat9,
        subcat10,
        subcat11,
        subcat12,
        subcat13
    )

    return listSubcategories
}