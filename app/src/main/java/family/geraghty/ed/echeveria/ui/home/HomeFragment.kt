package family.geraghty.ed.echeveria.ui.home

import android.os.Bundle
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.versionedparcelable.R
import family.geraghty.ed.echeveria.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val REQUEST_PERMISSIONS_CODE = 69

    private fun checkOrRequestPermissions() {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
        )

        if (permissions.any {
            ContextCompat.checkSelfPermission(requireContext(), it) != PackageManager.PERMISSION_GRANTED
            }) {
            requestPermissions(permissions, REQUEST_PERMISSIONS_CODE)
        } else  {
            getCellInfo()
        }
    }

    @Deprecated("")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSIONS_CODE && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            getCellInfo()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCellInfo() {
        val telephonyManager = requireContext().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val allCellInfo = telephonyManager.allCellInfo

        val cellInfoString = StringBuilder()
        for (cellInfo in allCellInfo) {
            cellInfoString.append(cellInfo.toString()).append("\n")
        }

        updateUI(cellInfoString.toString())
    }

    private fun updateUI(cellInfo: String) {
        binding.textHome.text = cellInfo
    }
}