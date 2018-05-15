package es.letmeparkparking.presentation.main

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import es.letmeparkparking.App
import es.letmeparkparking.data.models.login.UserInfoToSave
import es.letmeparkparking.data.repositories.preferences.PreferencesRepository
import javax.inject.Inject

@InjectViewState
class MainPresenter : MvpPresenter<MainView>() {

    @Inject
    lateinit var preferencesRepository: PreferencesRepository

    init {
        App.ComponentProvider.appComponent.inject(this)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        val userInfo = preferencesRepository.getUserInfo()
        checkIsUserHadCarAndCard(userInfo)
        viewState.setLoginState(userInfo)
    }

    fun logOut(){
        preferencesRepository.logOut()
        viewState.logOut()
    }

    private fun checkIsUserHadCarAndCard(userInfoToSave: UserInfoToSave?){
        userInfoToSave?.let {
            with(it.configuration){
                if (!hasEnabledCar /*|| !hasEnabledPaymentMethod*/ ){
                    viewState.openWelcomeScreen()
                }
            }
        }
    }



}