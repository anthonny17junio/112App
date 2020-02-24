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


//enum class Subcategories(val subcategories: List<Subcategory>?) {
//    CRIME_SUBCATEGORIES(getSubcategories(Category.CRIME)),
//    ACCIDENT_SUBCATEGORIES(getSubcategories(Category.ACCIDENT)),
//    MEDICAL_URGENCY_SUBCATEGORIES(getSubcategories(Category.MEDICAL_URGENCY))
//}

//
//private fun getSubcategories(category: Category): List<Subcategory>? {
//    return when (category) {
//        Category.CRIME ->
//            getCrimeSubcategories()
//        Category.ACCIDENT ->
//            getAccidentSubcategories()
//        Category.MEDICAL_URGENCY ->
//            getMedicalUrgencySubcategories()
//        else ->
//            null
//    }
//}

private fun getCrimeSubcategories(): List<Subcategory> {
    val listSubcategories: List<Subcategory>

    val subcat1 = Subcategory(
        R.string.subcategory_accident_car_with_injured,
        R.drawable.ic_subcategory_accident_car_with_injured
    ) //Borrar esta subcategoría
//    val subcat1 = Subcategory(1, 1) //Robo, atraco, asalto
    val subcat2 = Subcategory(1, 1) //Vandalismo, daño
    val subcat3 = Subcategory(1, 1) //Agresión, pelea
    val subcat4 = Subcategory(1, 1) //Agresión sexual
    val subcat5 = Subcategory(1, 1) //Violencia de género
    val subcat6 = Subcategory(1, 1) //Acoso escolar
    val subcat7 = Subcategory(1, 1) //Radicalismos
    listSubcategories = listOf(subcat1, subcat1, subcat1)
//    listSubcategories = listOf(subcat1, subcat2, subcat3, subcat4, subcat5, subcat6, subcat7)

    return listSubcategories
}

private fun getAccidentSubcategories(): List<Subcategory> {
    val listSubcategories: List<Subcategory>

    val subcat1 = Subcategory(
        R.string.subcategory_accident_car_with_injured,
        R.drawable.ic_subcategory_accident_car_with_injured
    ) //Accidente de tráfico con heridos
    val subcat2 = Subcategory(1, 1) //Accidente de tráfico sin heridos
    val subcat3 = Subcategory(1, 1) //Incendio de vehículo
    val subcat4 = Subcategory(1, 1) //Accidente en la calle
    val subcat5 = Subcategory(1, 1) //Accidente en la vivienda
    val subcat6 = Subcategory(1, 1) //Incendio en la vivienda/edificio
    val subcat7 = Subcategory(1, 1) //Encerrado en la vivienda
    val subcat8 = Subcategory(1, 1) //Encerrado en el ascensor
    val subcat9 = Subcategory(1, 1) //Accidente / Rescate en el monte
    val subcat10 = Subcategory(1, 1) //Extraviado en el monte
    val subcat11 = Subcategory(1, 1) //Salvamento / rescate acuático
    val subcat12 = Subcategory(1, 1) //Ataque de animal
    val subcat13 = Subcategory(1, 1) //Otros
    listSubcategories = listOf(subcat1, subcat1)
//    listSubcategories = listOf(
//        subcat1,
//        subcat2,
//        subcat3,
//        subcat4,
//        subcat5,
//        subcat6,
//        subcat7,
//        subcat8,
//        subcat9,
//        subcat10,
//        subcat11,
//        subcat12,
//        subcat13
//    )

    return listSubcategories
}

private fun getMedicalUrgencySubcategories(): List<Subcategory> {
    val listSubcategories: List<Subcategory>

    val subcat1 = Subcategory(
        R.string.subcategory_accident_car_with_injured,
        R.drawable.ic_subcategory_accident_car_with_injured
    ) //Borrar esta subcategoría
//    val subcat1 = Subcategory(1, 1) //Pérdida de conocimiento / desmayo
    val subcat2 = Subcategory(1, 1) //Dolor de pecho / infarto
    val subcat3 = Subcategory(1, 1) //Parálisis
    val subcat4 = Subcategory(1, 1) //Dificultad al respirar
    val subcat5 = Subcategory(1, 1) //Atragantamiento
    val subcat6 = Subcategory(1, 1) //Convulsiones
    val subcat7 = Subcategory(1, 1) //Fractura / traumatismo
    val subcat8 = Subcategory(1, 1) //Hemorragia
    val subcat9 = Subcategory(1, 1) //Intoxicación
    val subcat10 = Subcategory(1, 1) //Parto inminente
    val subcat11 = Subcategory(1, 1) //Electrocución
    val subcat12 = Subcategory(1, 1) //Quemadura
    val subcat13 = Subcategory(1, 1) //Ataque de animal
    listSubcategories = listOf(subcat1, subcat1, subcat1, subcat1)
//    listSubcategories = listOf(
//        subcat1,
//        subcat2,
//        subcat3,
//        subcat4,
//        subcat5,
//        subcat6,
//        subcat7,
//        subcat8,
//        subcat9,
//        subcat10,
//        subcat11,
//        subcat12,
//        subcat13
//    )

    return listSubcategories
}