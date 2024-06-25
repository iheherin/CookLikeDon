package com.projects.android.recipebook.view.single

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.projects.android.recipebook.R
import com.projects.android.recipebook.databinding.FragmentSingleRecipeBinding
import com.projects.android.recipebook.databinding.ItemSingleIngredientBinding
import com.projects.android.recipebook.model.enums.UnitOfMeasure
import com.projects.android.recipebook.utils.PictureUtils
import com.projects.android.recipebook.utils.PictureUtils.Companion.getScaledBitmap
import kotlinx.coroutines.launch

class SingleRecipeFragment : Fragment() {


	private val args: SingleRecipeFragmentArgs by navArgs()


	private val singleRecipeViewModel: SingleRecipeViewModel by viewModels {
		SingleRecipeViewModelFactory(args.recipeID)
	}


	private var _binding: FragmentSingleRecipeBinding? = null
	private val binding
		get() = checkNotNull(_binding) {
			"Cannot access binding because it is null. Is the view visible?"
		}

	private var _bindingIngredientsList = mutableListOf<ItemSingleIngredientBinding?>()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View {
		_binding = FragmentSingleRecipeBinding.inflate(layoutInflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)


		setupMenu()

		viewLifecycleOwner.lifecycleScope.launch {
			viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
				singleRecipeViewModel.state.collect { state ->
					state?.apply {
						recipe?.let {
							binding.apply {

								if (nameSingle.text.toString() != recipe!!.name) {
									nameSingle.text = recipe!!.name
								}


								if (courseSingle.text.toString() != recipe!!.course.toString()) {
									courseSingle.text = recipe!!.course.toString()
								}


								if (prepTimeSingle.text.toString() != recipe!!.preparationTime.toString()) {
									prepTimeSingle.text = recipe!!.preparationTime.toString()
								}


								if (portionsSingle.text.toString() != recipe!!.portions) {
									portionsSingle.text = recipe!!.portions
								}


								if (recipe!!.isCooked) {
									if (isCookedSingle.text.toString() != resources.getString(R.string.single_recipe_fragment_isCooked)) {
										isCookedSingle.text = resources.getString(R.string.single_recipe_fragment_isCooked)
										isCookedPictureSingle.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.is_fried))
									}
								} else {
									if (isCookedSingle.text.toString() != resources.getString(R.string.single_recipe_fragment_isNotCooked)) {
										isCookedSingle.text = resources.getString(R.string.single_recipe_fragment_isNotCooked)
										isCookedPictureSingle.setImageDrawable(
											AppCompatResources.getDrawable(
												requireContext(), R.drawable.is_not_fried
											)
										)
									}
								}


								if (recipe!!.isVeg) {
									if (isVegSingle.text.toString() != resources.getString(R.string.single_recipe_fragment_isVeg)) {
										isVegSingle.text = resources.getString(R.string.single_recipe_fragment_isVeg)
										isVegPictureSingle.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.is_veg))
									}
								} else {
									if (isVegSingle.text.toString() != resources.getString(R.string.single_recipe_fragment_isNotVeg)) {
										isVegSingle.text = resources.getString(R.string.single_recipe_fragment_isNotVeg)
										isVegPictureSingle.setImageDrawable(
											AppCompatResources.getDrawable(
												requireContext(), R.drawable.is_not_veg
											)
										)
									}
								}


								for (i in recipe!!.ingredientsList.indices) {
									val ingredient = recipe!!.ingredientsList[i]
									var bindingIngredients = if (_bindingIngredientsList.lastIndex >= i) _bindingIngredientsList[i] else null

									if (bindingIngredients == null) {
										bindingIngredients = ItemSingleIngredientBinding.inflate(layoutInflater)
										ingredientsContainerSingle.addView(bindingIngredients.root)
										_bindingIngredientsList.add(bindingIngredients)
									}
									bindingIngredients.apply {
										if (nameIngredientItemSingle.text.toString() != ingredient.name) {
											nameIngredientItemSingle.text = ingredient.name
										}

										if (ingredient.unitOfMeasure != UnitOfMeasure.TO_TASTE) {
											if (quantityIngredientItemSingle.text.toString() != ingredient.quantity) {
												quantityIngredientItemSingle.text = ingredient.quantity
											}
											if (unitOfMeasureIngredientItemSingle.text != ingredient.unitOfMeasure.toString()) {
												unitOfMeasureIngredientItemSingle.text = ingredient.unitOfMeasure.toString()
											}
										} else {
											if (quantityIngredientItemSingle.text != UnitOfMeasure.TO_TASTE.toString()) {
												quantityIngredientItemSingle.text = UnitOfMeasure.TO_TASTE.toString()
											}
											if (unitOfMeasureIngredientItemSingle.text != "") {
												unitOfMeasureIngredientItemSingle.text = ""
											}
										}
									}
								}

								for (i in recipe!!.ingredientsList.lastIndex + 1.._bindingIngredientsList.lastIndex) {
									ingredientsContainerSingle.removeView(_bindingIngredientsList[i]!!.root)
									_bindingIngredientsList.removeAt(i)
								}


								if (preparationSingle.text.toString() != recipe!!.preparation) {
									preparationSingle.text = recipe!!.preparation
								}


								if (photoSingle.tag != recipe!!.pictureFileName) { // Update photo only when the name is different
									val photoFile = recipe!!.pictureFileName?.let {
										PictureUtils.getPicture(requireContext(), it)
									}
									if (photoFile?.exists() == true) {
										photoSingle.doOnLayout { measuredView ->
											val scaledBitmap = getScaledBitmap(photoFile.path, measuredView.width, measuredView.height)
											photoSingle.setImageBitmap(scaledBitmap)
											photoSingle.tag = recipe!!.pictureFileName
										}
									} else {
										photoSingle.setImageBitmap(null)
										photoSingle.tag = null
									}
								}
							}
						}
					}
				}
			}
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
		for (i in _bindingIngredientsList.indices) {
			_bindingIngredientsList[i] = null
		}
		_bindingIngredientsList.clear()
	}


	private fun setupMenu() {
		(requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
			override fun onPrepareMenu(menu: Menu) {

			}

			override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
				menuInflater.inflate(R.menu.fragment_menu_single_recipe, menu)
			}

			override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
				return when (menuItem.itemId) {
					R.id.delete_recipe -> {
						AlertDialog.Builder(requireContext()).setTitle("Confirm to Delete?").setIcon(R.drawable.ic_baseline_dangerous_24)
							.setPositiveButton(android.R.string.ok) { _, _ ->
								viewLifecycleOwner.lifecycleScope.launch {
									singleRecipeViewModel.deleteRecipe(requireContext())
								}
								findNavController().navigateUp()
							}.setNegativeButton(android.R.string.cancel, null).show()
						true
					}

					R.id.edit_recipe -> {
						findNavController().navigate(SingleRecipeFragmentDirections.fromSingleRecipeFragmentToAddRecipeFragment(singleRecipeViewModel.state.value!!.recipe!!.id))
						true
					}

					else -> false
				}
			}
		}, viewLifecycleOwner, Lifecycle.State.RESUMED)
	}
}