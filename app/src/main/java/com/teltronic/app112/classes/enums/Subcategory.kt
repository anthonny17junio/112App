package com.teltronic.app112.classes.enums

import com.teltronic.app112.R

enum class Subcategory(var idTitle: Int, var idIcon: Int?, var category: Category?, val id: Int) {
    //*************************************************************
    //CRIME
    //*************************************************************
    //Robo, atraco, asalto
    CRIME_ROBBERY(
        R.string.subcategory_crime_robbery,
        R.drawable.ic_subcategory_crime_robbery,
        Category.CRIME,
        1
    ),

    //Vandalismo, daño
    CRIME_VANDALISM(
        R.string.subcategory_crime_vandalism,
        R.drawable.ic_subcategory_crime_vandalism,
        Category.CRIME,
        2
    ),

    //Agresión, pelea
    CRIME_AGGRESSION(
        R.string.subcategory_crime_aggression,
        R.drawable.ic_subcategory_crime_agression,
        Category.CRIME,
        3
    ),

    //Agresión sexual
    CRIME_SEXUAL_ASSAULT(
        R.string.subcategory_crime_sexual_assault,
        R.drawable.ic_subcategory_crime_sexual_assault,
        Category.CRIME,
        4
    ),

    //Violencia de género
    CRIME_GENDER_VIOLENCE(
        R.string.subcategory_crime_gender_violence,
        R.drawable.ic_subcategory_crime_gender_violence,
        Category.CRIME,
        5
    ),

    //Acoso escolar
    CRIME_BULLYING(
        R.string.subcategory_crime_bullying,
        R.drawable.ic_subcategory_crime_bullying,
        Category.CRIME,
        6
    ),

    //Radicalismos
    CRIME_RADICALISMS(
        R.string.subcategory_crime_radicalisms,
        R.drawable.ic_subcategory_crime_radicalisms,
        Category.CRIME,
        7
    ),

    //*************************************************************
    //CRIME
    //*************************************************************
    //Accidente de tráfico con heridos
    ACCIDENT_CAR_WITH_INJURED(
        R.string.subcategory_accident_car_with_injured,
        R.drawable.ic_subcategory_accident_car_with_injured,
        Category.ACCIDENT,
        8
    ),

    //Accidente de tráfico sin heridos
    ACCIDENT_CAR_WITHOUT_INJURED(
        R.string.subcategory_accident_car_without_injured,
        R.drawable.ic_subcategory_accident_car_without_injured,
        Category.ACCIDENT,
        9
    ),

    //Incendio de vehículo
    ACCIDENT_VEHICLE_FIRE(
        R.string.subcategory_accident_vehicle_fire,
        R.drawable.ic_subcategory_accident_vehicle_fire,
        Category.ACCIDENT,
        10
    ),

    //Accidente en la calle
    ACCIDENT_STREET_ACCIDENT(
        R.string.subcategory_accident_street_accident,
        R.drawable.ic_subcategory_accident_street_accident,
        Category.ACCIDENT,
        11
    ),

    //Accidente en la vivienda
    ACCIDENT_HOUSING_ACCIDENT(
        R.string.subcategory_accident_housing_accident,
        R.drawable.ic_subcategory_accident_housing_accident,
        Category.ACCIDENT,
        12
    ),

    //Incendio en la vivienda/edificio
    ACCIDENT_HOUSE_FIRE(
        R.string.subcategory_accident_house_fire,
        R.drawable.ic_subcategory_accident_house_fire,
        Category.ACCIDENT,
        13
    ),

    //Encerrado en la vivienda
    ACCIDENT_LOCKED_INSIDE_HOUSE(
        R.string.subcategory_accident_locked_inside_house,
        R.drawable.ic_subcategory_accident_locked_inside_house,
        Category.ACCIDENT,
        14
    ),

    //Encerrado en el ascensor
    ACCIDENT_LOCKED_INSIDE_ELEVATOR(
        R.string.subcategory_accident_locked_inside_elevator,
        R.drawable.ic_subcategory_accident_locked_inside_elevator,
        Category.ACCIDENT,
        15
    ),

    //Accidente / Rescate en el monte
    ACCIDENT_MOUNTAIN_ACCIDENT(
        R.string.subcategory_accident_mountain_accident,
        R.drawable.ic_subcategory_accident_mountain_accident,
        Category.ACCIDENT,
        16
    ),

    //Extraviado en el monte
    ACCIDENT_MOUNTAIN_LOST(
        R.string.subcategory_accident_mountain_lost,
        R.drawable.ic_subcategory_accident_mountain_lost,
        Category.ACCIDENT,
        17
    ),

    //Salvamento / rescate acuático
    ACCIDENT_AQUATIC_RESCUE(
        R.string.subcategory_accident_aquatic_rescue,
        R.drawable.ic_subcategory_accident_aquatic_rescue,
        Category.ACCIDENT,
        18
    ),

    //Ataque de animal
    ACCIDENT_ANIMAL_ATTACK(
        R.string.subcategory_accident_animal_attack,
        R.drawable.ic_subcategory_accident_animal_attack,
        Category.ACCIDENT,
        19
    ),

    //Otros
    ACCIDENT_OTHER(
        R.string.subcategory_accident_other,
        R.drawable.ic_subcategory_accident_other,
        Category.ACCIDENT,
        20
    ),

    //*************************************************************
    //MEDICAL_URGENCY
    //*************************************************************
    //Pérdida de conocimiento / desmayo
    MEDICAL_URGENCY_FAINTING(
        R.string.subcategory_medical_urgency_fainting,
        R.drawable.ic_subcategory_medical_urgency_fainting,
        Category.MEDICAL_URGENCY,
        21
    ),

    //Dolor de pecho / infarto
    MEDICAL_URGENCY_HEART_ATTACK(
        R.string.subcategory_medical_urgency_heart_attack,
        R.drawable.ic_subcategory_medical_urgency_heart_attack,
        Category.MEDICAL_URGENCY,
        22
    ),

    //Parálisis
    MEDICAL_URGENCY_PARALYSIS(
        R.string.subcategory_medical_urgency_paralysis,
        R.drawable.ic_subcategory_medical_urgency_paralysis,
        Category.MEDICAL_URGENCY,
        23
    ),

    //Dificultad al respirar
    MEDICAL_URGENCY_DIFFICULTY_BREATHING(
        R.string.subcategory_medical_urgency_difficulty_breathing,
        R.drawable.ic_subcategory_medical_urgency_difficulty_breathing,
        Category.MEDICAL_URGENCY,
        24
    ),

    //Atragantamiento
    MEDICAL_URGENCY_CHOCKING(
        R.string.subcategory_medical_urgency_chocking,
        R.drawable.ic_subcategory_medical_urgency_chocking,
        Category.MEDICAL_URGENCY,
        25
    ),

    //Convulsiones
    MEDICAL_URGENCY_SEIZURES(
        R.string.subcategory_medical_urgency_seizures,
        R.drawable.ic_subcategory_medical_urgency_seizures,
        Category.MEDICAL_URGENCY,
        26
    ),

    //Fractura / traumatismo
    MEDICAL_URGENCY_FRACTURE(
        R.string.subcategory_medical_urgency_fracture,
        R.drawable.ic_subcategory_medical_urgency_fracture,
        Category.MEDICAL_URGENCY,
        27
    ),

    //Hemorragia
    MEDICAL_URGENCY_HEMORRHAGE(
        R.string.subcategory_medical_urgency_hemorrhage,
        R.drawable.ic_subcategory_medical_urgency_hemorrhage,
        Category.MEDICAL_URGENCY,
        28
    ),

    //Intoxicación
    MEDICAL_URGENCY_POISONING(
        R.string.subcategory_medical_urgency_poisoning,
        R.drawable.ic_subcategory_medical_urgency_poisoning,
        Category.MEDICAL_URGENCY,
        29
    ),

    //Parto inminente
    MEDICAL_URGENCY_IMMINENT_CHILDBIRTH(
        R.string.subcategory_medical_urgency_imminent_childbirth,
        R.drawable.ic_subcategory_medical_urgency_imminent_childbirth,
        Category.MEDICAL_URGENCY,
        30
    ),

    //Electrocución
    MEDICAL_URGENCY_ELECTROCUTION(
        R.string.subcategory_medical_urgency_electrocution,
        R.drawable.ic_subcategory_medical_urgency_electrocution,
        Category.MEDICAL_URGENCY,
        31
    ),

    //Quemadura
    MEDICAL_URGENCY_BURN(
        R.string.subcategory_medical_urgency_burn,
        R.drawable.ic_subcategory_medical_urgency_burn,
        Category.MEDICAL_URGENCY,
        32
    ),

    //Ataque de animal
    MEDICAL_URGENCY_ANIMAL_ATTACK(
        R.string.subcategory_medical_urgency_animal_attack,
        R.drawable.ic_subcategory_medical_urgency_animal_attack,
        Category.MEDICAL_URGENCY,
        33
    ),

    //*************************************************************
    //OTHER
    //*************************************************************
    //Otro
    OTHER(
        R.string.txt_other,
        R.drawable.ic_subcategory_accident_other,
        Category.OTHER,
        34
    ),

    EMPTY(
        R.string.empty,
        null,
        Category.EMPTY,
        35
    );

    companion object {
        fun getById(id: Int): Subcategory? {
            for (subcat in values()) {
                if (subcat.id == id) {
                    return subcat
                }
            }
            return null
        }
    }
}