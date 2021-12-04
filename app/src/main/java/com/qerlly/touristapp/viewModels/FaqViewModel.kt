package com.qerlly.touristapp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qerlly.touristapp.model.FaqModel
import com.qerlly.touristapp.repositories.FaqRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FaqViewModel @Inject constructor(
    faqRepository: FaqRepository,
) : ViewModel() {

    val faqState: StateFlow<List<FaqModel>?> =
        faqRepository.getAll().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)
}