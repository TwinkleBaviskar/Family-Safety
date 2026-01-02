package com.example.familysafety

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView

class GuardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_guard, container, false)

        val pinkcard = view.findViewById<CardView>(R.id.pinkcard)
        pinkcard.setOnClickListener {
            val intent = Intent(requireContext(), SosActivity::class.java)
            startActivity(intent)
        }

        val greencard = view.findViewById<CardView>(R.id.greencard)
        greencard.setOnClickListener {
            val intent = Intent(requireContext(), GaurdActivity::class.java)
            startActivity(intent)
        }

        val card = view.findViewById<CardView>(R.id.card)
        card.setOnClickListener {
            val intent = Intent(requireContext(), LocationActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = GuardFragment()
    }
    }

