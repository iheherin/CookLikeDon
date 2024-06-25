package com.projects.android.recipebook.model.enums

import com.google.gson.annotations.SerializedName

enum class UnitOfMeasure(value: String) {
	@SerializedName("0")
	GRAM("гр"),

	@SerializedName("1")
	MILLILITERS("мл"),

	@SerializedName("2")
	TO_TASTE("по вкусу");

	private val valueString: String = value

	override fun toString(): String {
		return valueString
	}

	companion object {

		fun of(string: String): UnitOfMeasure? {
			return when (string) {
				GRAM.valueString -> GRAM
				MILLILITERS.valueString -> MILLILITERS
				TO_TASTE.valueString -> TO_TASTE
				else -> null
			}
		}
	}
}