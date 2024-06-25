package com.projects.android.recipebook.view.list

import android.graphics.drawable.Icon
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import com.projects.android.recipebook.R
import com.projects.android.recipebook.databinding.FragmentListRecipesBinding
import kotlinx.coroutines.launch

class ListRecipesFragment : Fragment() {

	// VIEW MODEL
	private val listRecipesViewModel: ListRecipesViewModel by viewModels()

	// VIEW BINDING
	private var _binding: FragmentListRecipesBinding? = null
	private val binding
		get() = checkNotNull(_binding) {
			"Cannot access binding because it is null. Is the view visible?"
		}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View {
		// RECYCLER VIEW
		_binding = FragmentListRecipesBinding.inflate(inflater, container, false)
		binding.recipesRecyclerViewList.layoutManager = LinearLayoutManager(context)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.apply {

			expandFiltersList.setOnClickListener {
				if (filtersLayoutList.visibility == VISIBLE) {
					filtersLayoutList.visibility = GONE
					expandFiltersList.setImageIcon(Icon.createWithResource(context, R.drawable.ic_baseline_keyboard_arrow_down_24))
				} else {
					filtersLayoutList.visibility = VISIBLE
					expandFiltersList.setImageIcon(Icon.createWithResource(context, R.drawable.ic_baseline_keyboard_arrow_up_24))
				}
			}

			addRecipeFABList.setOnClickListener {
				findNavController().navigate(ListRecipesFragmentDirections.fromListRecipesFragmentToAddRecipeFragment(-2))
			}

			informationFragment.setOnClickListener {
				findNavController().navigate(ListRecipesFragmentDirections.fromListRecipesFragmentToInformationFragment())
			}
		}

		viewLifecycleOwner.lifecycleScope.launch {
			viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
				listRecipesViewModel.recipes.collect { recipes ->
					binding.recipesRecyclerViewList.adapter = ListRecipesAdapter(recipes, requireContext()) { recipeID ->
						findNavController().navigate(ListRecipesFragmentDirections.fromListRecipesFragmentToSingleRecipeFragment(recipeID))
					}
					// Separator between items
					binding.recipesRecyclerViewList.addItemDecoration(DividerItemDecoration(context, VERTICAL))
				}
			}
		}
	}

	// VIEW BINDING
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}
