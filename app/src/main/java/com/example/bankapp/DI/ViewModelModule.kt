package com.example.bankapp.DI

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bankapp.features.account.account.AccountViewModel
import com.example.bankapp.features.account.accountEdit.AccountEditViewModel
import com.example.bankapp.features.categories.CategoriesViewModel
import com.example.bankapp.features.expenses.ExpensesViewModel
import com.example.bankapp.features.history.HistoryViewModel
import com.example.bankapp.features.income.IncomeViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.reflect.KClass


@MapKey
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class ViewModelKey(val value: KClass<out ViewModel>)


@Module
interface ViewModelModule{
    @Binds
    @Singleton
    @IntoMap
    @ViewModelKey(AccountViewModel::class) //LoginViewModel::class
     fun bindAccountViewModel(viewModel: AccountViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AccountEditViewModel::class) //LoginViewModel222::class
     fun bindAccountEditViewModel(viewModel: AccountEditViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CategoriesViewModel::class) //LoginViewModel222::class
     fun bindCategoriesViewModel(viewModel: CategoriesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ExpensesViewModel::class) //LoginViewModel::class
     fun bindExpensesViewModel(viewModel: ExpensesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(IncomeViewModel::class) //LoginViewModel::class
     fun bindIncomeViewModel(viewModel: IncomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HistoryViewModel::class) //LoginViewModel::class
     fun bindHistoryViewModel(viewModel: HistoryViewModel): ViewModel

    //сюда новые viewModel

    @Binds
    fun bindsDaggerViewModelFactory(daggerViewModelFactory: DaggerViewModelFactory): ViewModelProvider.Factory


}


class DaggerViewModelFactory @Inject constructor(
    private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val creator = creators[modelClass] ?: creators.entries.firstOrNull() {
            modelClass.isAssignableFrom(it.key)
        } ?.value ?: throw IllegalArgumentException("Unknown ViewModel class $modelClass")
        return creator.get() as T
    }
}