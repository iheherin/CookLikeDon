package com.projects.android.recipebook.model.enums

enum class Course(value: String) {
	GARNIER("Гарнир"), FIRST("Первое"), SECOND("Второе"), SNACK("Закуски"), DESSERT("Десерт");

	private val valueString: String = value

	override fun toString(): String {
		return valueString
	}
}