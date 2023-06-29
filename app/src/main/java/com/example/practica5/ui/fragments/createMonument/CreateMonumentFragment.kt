package com.example.practica5.ui.fragments.createMonument

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.practica5.R
import com.example.practica5.databinding.FragmentCreateMonumentBinding
import com.example.practica5.domain.model.vo.ImageVO
import com.example.practica5.ui.activity.NavigationActivity
import com.example.practica5.ui.adapter.ImageAdapter
import com.example.practica5.utils.MonumentsConstant.ADD_MONUMENT_MESSAGE
import com.example.practica5.utils.MonumentsConstant.CLOSE_OPTION
import com.example.practica5.utils.MonumentsConstant.DATE_FORMAT
import com.example.practica5.utils.MonumentsConstant.DENIED_PERMISSIONS
import com.example.practica5.utils.MonumentsConstant.EMPTY_INFO
import com.example.practica5.utils.MonumentsConstant.EOF
import com.example.practica5.utils.MonumentsConstant.IMAGE_BUFFER
import com.example.practica5.utils.MonumentsConstant.INPUT_EMPTY
import com.example.practica5.utils.MonumentsConstant.MAX_CHARACTERS_DESC_INPUT
import com.example.practica5.utils.MonumentsConstant.MAX_CHARACTERS_NAME_CITY_INPUT
import com.example.practica5.utils.MonumentsConstant.MONUMENT_ZOOM
import com.example.practica5.utils.MonumentsConstant.NEW_MONUMENT_TITLE
import com.example.practica5.utils.MonumentsConstant.OPEN_CAMERA_OPTION
import com.example.practica5.utils.MonumentsConstant.OPEN_GALLERY_OPTION
import com.example.practica5.utils.MonumentsConstant.PERMISSION_REQUEST_CAMERA
import com.example.practica5.utils.MonumentsConstant.SELECT_GALLERY
import com.example.practica5.utils.MonumentsConstant.TAKE_PHOTO
import com.example.practica5.utils.MonumentsConstant.PERMISSION_REQUEST_READ_EXTERNAL_STORAGE
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class CreateMonumentFragment : Fragment() {

    private val binding by lazy { FragmentCreateMonumentBinding.inflate(layoutInflater) }
    private val createMonumentViewModel: CreateMonumentViewModel by activityViewModels()

    private lateinit var imageAdapter: ImageAdapter
    private lateinit var mapView: MapView
    private var googleMap: GoogleMap? = null
    private var currentPhotoPath: String? = null
    private lateinit var selectedCountry: String
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private var requestedPermission: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mapView = binding.newMonumentMapViewLocation
        mapView.onCreate(savedInstanceState)
        mapView.onResume()

        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                when (requestedPermission) {
                    Manifest.permission.READ_MEDIA_IMAGES -> openGallery()
                    Manifest.permission.CAMERA -> openCamera()
                }
            } else {
                Toast.makeText(requireContext(), DENIED_PERMISSIONS, Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initUi()
        observeData()
        setSpinner()
        setBtnSaveEnabled()
    }

    private fun moveMarker(location: LatLng) {
        googleMap?.clear()
        googleMap?.addMarker(MarkerOptions().position(location))
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(location, MONUMENT_ZOOM))

        createMonumentViewModel.setLocation(location)
    }

    private fun initListeners() = with(binding) {
        newMonumentImgNewImage.setOnClickListener {
            showImageSelectionDialog()
        }

        newMonumentBtnSave.setOnClickListener {
            createMonumentViewModel.createNewMonument(
                newMonumentInputMonumentName.text.toString(),
                newMonumentInputMonumentDesc.text.toString(),
                newMonumentInputMonumentCity.text.toString(),
                selectedCountry,
                newMonumentBtnSave.context
            )
            Toast.makeText(newMonumentBtnSave.context, ADD_MONUMENT_MESSAGE, Toast.LENGTH_SHORT).show()
        }

        newMonumentInputMonumentName.addTextChangedListener(monumentNameTextWatcher)
        newMonumentInputMonumentCity.addTextChangedListener(monumentCityTextWatcher)
        newMonumentInputMonumentDesc.addTextChangedListener(monumentDescTextWatcher)
    }

    private fun setUpRecyclerView() {
        imageAdapter = ImageAdapter()
        imageAdapter.setOnLongClickListener { image ->
            onLongClickImage(image)
        }
        binding.newMonumentListImages.adapter = imageAdapter
    }

    private fun initUi() {
        initToolbar()
        setUpRecyclerView()
        initMapView()
    }

    private fun initMapView() {
        mapView.getMapAsync { map ->
            googleMap = map
            val location = createMonumentViewModel.locationLiveData.value
            location?.let {
                moveMarker(location)

                googleMap?.setOnMapLongClickListener { newLocation ->
                    moveMarker(newLocation)
                }
            }
        }
    }

    private fun initToolbar() {
        val mainToolbar = (requireActivity() as NavigationActivity).binding.appBarNavigation.toolbar
        mainToolbar.title = NEW_MONUMENT_TITLE
    }

    private fun onLongClickImage(image: ImageVO) {
        val imgDeletedPosition = createMonumentViewModel.deleteImage(image)
        imageAdapter.notifyItemRemoved(imgDeletedPosition)
    }

    private fun observeData() {
        createMonumentViewModel.imageListLiveData.observe(viewLifecycleOwner) { images ->
            imageAdapter.submitList(images)
        }

        createMonumentViewModel.getInsertionComplete().observe(viewLifecycleOwner) { isComplete ->
            if (isComplete) {
                createMonumentViewModel.setInsertionToFalse()
                findNavController().navigate(R.id.action_createMonumentFragment_to_nav_monuments)
            }
        }
    }

    private fun showImageSelectionDialog() {
        val options = arrayOf<CharSequence>(
            SELECT_GALLERY,
            TAKE_PHOTO,
            getString(R.string.common__cancel)
        )

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.createMonument__select_image))
        builder.setItems(options) { dialog, item ->
            when (item) {
                OPEN_GALLERY_OPTION -> openGallery()
                OPEN_CAMERA_OPTION -> openCamera()
                CLOSE_OPTION -> dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun openGallery() {
        requestedPermission = Manifest.permission.READ_MEDIA_IMAGES
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickGalleryLauncher.launch(intent)
        } else {
            requestPermissionLauncher.launch(requestedPermission)
        }
    }

    private fun openCamera() {
        requestedPermission = Manifest.permission.CAMERA
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val photoFile: File? = try {
                createImageFile()
            } catch (ex: IOException) {
                ex.printStackTrace()
                null
            }
            photoFile?.let {
                val photoURI: Uri = FileProvider.getUriForFile(
                    requireContext(),
                    getString(R.string.newMonuments__file_provider, requireContext().packageName),
                    it
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                takePictureLauncher.launch(intent)
            }
        } else {
            requestPermissionLauncher.launch(requestedPermission)
        }
    }

    private fun createImageFile(): File? {
        val timeStamp: String = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            getString(R.string.newMonuments__prefix_file, timeStamp),
            getString(R.string.newMonuments_suffix_file),
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                currentPhotoPath?.let { path ->
                    createMonumentViewModel.addImage(path)
                    imageAdapter.notifyItemChanged(imageAdapter.itemCount)
                }
            } else {
                currentPhotoPath?.let { path ->
                    val file = File(path)
                    file.delete()
                }
            }
            currentPhotoPath = null
        }

    private val pickGalleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri: Uri? = result.data?.data
                if (imageUri != null) {
                    val imagePath = copyGalleryImageToCameraDirectory(imageUri)
                    if (imagePath != null) {
                        createMonumentViewModel.addImage(imagePath)
                    }
                    imageAdapter.notifyItemChanged(imageAdapter.itemCount)
                }
            }
        }

    private fun copyGalleryImageToCameraDirectory(imageUri: Uri): String? {
        val inputStream = requireContext().contentResolver.openInputStream(imageUri)
        val outputFile = createImageFile()

        inputStream?.use { input ->
            val outputStream = FileOutputStream(outputFile)
            outputStream.use { output ->
                val buffer = ByteArray(IMAGE_BUFFER)
                var bytesRead: Int
                while (input.read(buffer).also { bytesRead = it } != EOF) {
                    output.write(buffer, 0, bytesRead)
                }
                output.flush()
            }
        }

        return outputFile?.absolutePath
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_READ_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                    openGallery()
                }
            }

            PERMISSION_REQUEST_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                }
            }
        }
    }

    private fun setSpinner() {
        val countriesArray = resources.getStringArray(R.array.countries_array)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, countriesArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        with(binding) {
            newMonumentSpinnerCountry.adapter = adapter

            newMonumentSpinnerCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    selectedCountry = parent?.getItemAtPosition(position).toString()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    selectedCountry = EMPTY_INFO
                }
            }
        }
    }

    private fun setBtnSaveEnabled() = with(binding) {
        val editTextList = listOf(
            newMonumentInputMonumentName,
            newMonumentInputMonumentDesc,
            newMonumentInputMonumentCity
        )

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                val allFieldsFilled = editTextList.all { it.text.toString().isNotEmpty() }
                newMonumentBtnSave.isEnabled = allFieldsFilled
            }
        }

        editTextList.forEach { it.addTextChangedListener(textWatcher) }
    }

    private val monumentNameTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(s: Editable?) {
            val currentLength = s?.length ?: INPUT_EMPTY
            with(binding) {
                newMonumentLabelMaxName.text = getString(R.string.newgame__max_50_text, currentLength.toString())
                if (currentLength == MAX_CHARACTERS_NAME_CITY_INPUT) {
                    newMonumentLabelMaxName.setTextColor(Color.RED)

                } else {
                    newMonumentLabelMaxName.setTextColor(Color.BLACK)
                }
            }
        }
    }

    private val monumentCityTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(s: Editable?) {
            val currentLength = s?.length ?: INPUT_EMPTY
            with(binding) {
                newMonumentLabelMaxCity.text = getString(R.string.newgame__max_50_text, currentLength.toString())
                if (currentLength == MAX_CHARACTERS_NAME_CITY_INPUT) {
                    newMonumentLabelMaxCity.setTextColor(Color.RED)

                } else {
                    newMonumentLabelMaxCity.setTextColor(Color.BLACK)
                }
            }
        }
    }

    private val monumentDescTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(s: Editable?) {
            val currentLength = s?.length ?: INPUT_EMPTY
            with(binding) {
                newMonumentLabelMaxDesc.text = getString(R.string.newgame__max_250_text, currentLength.toString())
                if (currentLength == MAX_CHARACTERS_DESC_INPUT) {
                    newMonumentLabelMaxDesc.setTextColor(Color.RED)

                } else {
                    newMonumentLabelMaxDesc.setTextColor(Color.BLACK)
                }
            }
        }
    }
}