package es.letmeparkparking.presentation.main

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Gravity
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import es.letmeparkparking.R
import es.letmeparkparking.data.models.login.UserInfoToSave
import es.letmeparkparking.presentation.base.BaseActivity
import es.letmeparkparking.presentation.base.BaseFragment
import es.letmeparkparking.presentation.booking_history.BookingHistoryFragment
import es.letmeparkparking.presentation.contact_us.ContactUsFragment
import es.letmeparkparking.presentation.main.MainActivity.Constants.OPEN_SETTINGS_EXTRA
import es.letmeparkparking.presentation.main.fragment.ParkingNetworkFragment
import es.letmeparkparking.presentation.profile.ProfileFragmentContainer
import es.letmeparkparking.presentation.promotions.PromotionsFragment
import es.letmeparkparking.presentation.reserved_parking.ReservedParkingFragment
import es.letmeparkparking.presentation.settings.SettingsFragment
import es.letmeparkparking.presentation.welcome.WelcomeActivity
import es.letmeparkparking.utils.LocaleManager
import es.letmeparkparking.utils.setupToolbar
import es.letmeparkparking.utils.showConfirmDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.include_toolbar.*

class MainActivity : BaseActivity(), MainView {

    @InjectPresenter
    lateinit var presenter: MainPresenter

    @ProvidePresenter
    fun providePresenter() = MainPresenter()

    private lateinit var mDrawerToggle: ActionBarDrawerToggle

    object Constants {
        const val OPEN_SETTINGS_EXTRA = "OPEN_SETTINGS_EXTRA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setListeners()
        setupToolbar(toolbar)
        initDrawer()
        if (intent.getBooleanExtra(OPEN_SETTINGS_EXTRA, false)){
            showFragment(SettingsFragment())
        } else {
            showFragment(ParkingNetworkFragment())
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mDrawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        mDrawerToggle.onConfigurationChanged(newConfig)
    }

    override fun logOut() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun setLoginState(userInfoToSave: UserInfoToSave?) {
        userInfoToSave?.let {
            textViewLogOut.visibility = View.VISIBLE
            textViewUsername.text = getString(R.string.main_user_name_template, userInfoToSave.firstName, userInfoToSave.lastName)
            relativeLayoutProfile.isClickable = true
            return
        }
        relativeLayoutProfile.isClickable = false
        textViewLogOut.visibility = View.GONE
        textViewUsername.text = getString(R.string.main_user_not_logged_yet)
    }

    override fun openWelcomeScreen() {
        startActivity(Intent(this, WelcomeActivity::class.java))
        finish()
    }

    fun changeUsername(username: String){
        textViewUsername.text = username
    }

    private fun setToolbarTitle(title: String) {
        textViewToolbarTitle.text = title
    }

    private fun showFragment(fragment: BaseFragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.linear_layout_fragment_container,
                        fragment.apply { setGetToolbarTitleCallback { setToolbarTitle(it) } },"")
                .commit()
        drawerLayout.closeDrawer( Gravity.START)
    }

    fun initDrawer(){
        mDrawerToggle = object : ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.common_open_on_phone, R.string.common_open_on_phone) {

            override fun onDrawerClosed(view: View) { supportInvalidateOptionsMenu() }

            override fun onDrawerOpened(drawerView: View) { supportInvalidateOptionsMenu() }
        }
        mDrawerToggle.isDrawerIndicatorEnabled = true
        drawerLayout.setDrawerListener(mDrawerToggle)
        mDrawerToggle.syncState()

    }

    private fun setListeners(){
        textViewParkingNetwork.setOnClickListener { showFragment(ParkingNetworkFragment()) }
        textViewBooking.setOnClickListener { showFragment(ReservedParkingFragment()) }
        textViewLogOut.setOnClickListener { showConfirmDialog(this, getString(R.string.warning_log_out), {presenter.logOut()}) }
        textViewHistory.setOnClickListener { showFragment(BookingHistoryFragment()) }
        textViewSettings.setOnClickListener { showFragment(SettingsFragment()) }
        textViewPromotions.setOnClickListener { showFragment(PromotionsFragment()) }
        textViewHelpAndContact.setOnClickListener { showFragment(ContactUsFragment()) }
        relativeLayoutProfile.setOnClickListener { showFragment(ProfileFragmentContainer()) }
    }

}
