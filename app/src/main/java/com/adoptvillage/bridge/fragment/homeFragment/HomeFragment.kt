package com.adoptvillage.bridge.fragment.homeFragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.transition.TransitionInflater
import com.adoptvillage.bridge.adapters.CardAdapter
import com.adoptvillage.bridge.models.CardModel
import com.adoptvillage.bridge.R
import com.adoptvillage.bridge.activity.ApplicationFormActivity
import com.adoptvillage.bridge.activity.MainActivity
import com.adoptvillage.bridge.fragment.applicationFormFragment.ApplicationFormStudentDetails
import com.adoptvillage.bridge.fragment.authFragment.SignUpFragment
import com.adoptvillage.bridge.fragment.profileFragment.ProfileFragment
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

    private lateinit var cardModelList: ArrayList<CardModel>
    private lateinit var cardAdapter: CardAdapter
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.explode)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        loadCards()
    }

    private fun loadCards()
    {
        cardModelList = ArrayList()

        cardModelList.add(CardModel("Abhi","Vatsal","300","Mr. A"))
        cardModelList.add(CardModel("Ankit","Abhishek","500","Mr. B"))
        cardModelList.add(CardModel("Raju","Rasmeet","1000","Mr. C"))
        cardModelList.add(CardModel("Humraz","John Doe","400","Mr. C"))
        cardAdapter = activity?.let { CardAdapter(it,cardModelList) }!!
        slideView.adapter = cardAdapter
        slideView.setPadding(20,10,20,10)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs=context!!.getSharedPreferences(
            activity?.getString(R.string.parent_package_name),
            Context.MODE_PRIVATE
        )

        btnAdoptVillageSetOnClickListener()
        btnSubmitApplicationSetOnClickListener()
        btnOnlyForDonor()
    }

    private fun btnSubmitApplicationSetOnClickListener() {
        btnSubmitApplication.setOnClickListener {
            val intent= Intent(context, ApplicationFormActivity::class.java)
            startActivity(intent)
        }
    }

/*
* Role 1 -> DONOR
* Role 2 -> RECIPIENT
* Role 3 -> MODERATOR
* */

    private fun btnOnlyForDonor() {
        when {
            prefs.getInt(activity?.getString(R.string.role), 0) == 1 -> {
                btnApplications.visibility=View.VISIBLE
                btnAdoptVillage.visibility=View.VISIBLE
                btnSubmitApplication.visibility=View.INVISIBLE
            }
            prefs.getInt(activity?.getString(R.string.role), 0) == 2 -> {
                btnApplications.visibility=View.INVISIBLE
                btnAdoptVillage.visibility=View.INVISIBLE
                btnSubmitApplication.visibility=View.VISIBLE
            }
            prefs.getInt(activity?.getString(R.string.role), 0) == 3 -> {
                btnApplications.visibility=View.INVISIBLE
                btnAdoptVillage.visibility=View.INVISIBLE
                btnSubmitApplication.visibility=View.INVISIBLE
            }
        }
    }

    private fun btnAdoptVillageSetOnClickListener() {
        btnAdoptVillage.setOnClickListener {
            activity?.supportFragmentManager?.popBackStackImmediate()
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.fl_wrapper, LocationFragment())?.addToBackStack(javaClass.name)?.commit()
        }
    }
}

