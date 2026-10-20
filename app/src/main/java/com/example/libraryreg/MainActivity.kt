package com.example.libraryreg

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.UUID

data class Member(
    val id: String,
    val name: String,
    val nimNip: String,
    val prodi: String,
    val email: String,
    val phone: String,
    val address: String,
    val memberType: String
)

sealed class Screen {
    object Home : Screen()
    object Register : Screen()
    object Management : Screen()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF4F7FC)
                ) {
                    LibraryRegApp()
                }
            }
        }
    }
}

@Composable
fun LibraryRegApp() {
    val memberList = remember {
        mutableStateListOf(
            Member(
                "1", "Brian", "20101101", "Informatika",
                "andi@mail.com", "08123456789", "Surabaya", "Mahasiswa"
            ),
            Member(
                "2", "Fatimah", "20101102", "Sistem Informasi",
                "siti@mail.com", "08123456780", "Sidoarjo", "Mahasiswa"
            ),
            Member(
                "3", "Nevan", "19890101", "Dosen",
                "budi@mail.com", "08123456781", "Gresik", "Dosen"
            )
        )
    }

    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
    var memberToEdit by remember { mutableStateOf<Member?>(null) }

    when (currentScreen) {
        is Screen.Home -> HomeScreen(
            totalMembers = memberList.size,
            onNavigateToRegister = {
                memberToEdit = null
                currentScreen = Screen.Register
            },
            onNavigateToManagement = {
                currentScreen = Screen.Management
            }
        )

        is Screen.Register -> RegistrationScreen(
            existingMember = memberToEdit,
            onBack = {
                currentScreen = Screen.Home
            },
            onSubmit = { newOrUpdatedMember ->
                val index = memberList.indexOfFirst {
                    it.id == newOrUpdatedMember.id
                }

                if (index != -1) {
                    memberList[index] = newOrUpdatedMember
                } else {
                    memberList.add(newOrUpdatedMember)
                }

                currentScreen = Screen.Management
            }
        )

        is Screen.Management -> ManagementScreen(
            memberList = memberList,
            onBack = {
                currentScreen = Screen.Home
            },
            onAddMember = {
                memberToEdit = null
                currentScreen = Screen.Register
            },
            onEditMember = { member ->
                memberToEdit = member
                currentScreen = Screen.Register
            },
            onDeleteMember = { member ->
                memberList.remove(member)
            }
        )
    }
}

@Composable
fun HomeScreen(
    totalMembers: Int,
    onNavigateToRegister: () -> Unit,
    onNavigateToManagement: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F7FC))
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFF1976D2),
                    shape = RoundedCornerShape(
                        bottomStart = 32.dp,
                        bottomEnd = 32.dp
                    )
                )
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    top = 38.dp,
                    bottom = 30.dp
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(58.dp)
                        .background(
                            Color.White,
                            RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.MenuBook,
                        contentDescription = "Logo LibraryReg",
                        modifier = Modifier.size(34.dp),
                        tint = Color(0xFF1976D2)
                    )
                }

                Column {
                    Text(
                        text = "LibraryReg",
                        fontSize = 27.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Text(
                        text = "Pendaftaran Anggota Perpustakaan",
                        fontSize = 12.sp,
                        color = Color(0xFFE3F2FD)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "SISTEM INFORMASI PERPUSTAKAAN",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp,
                color = Color(0xFFBBDEFB)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Selamat Datang!",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Kelola keanggotaan perpustakaan dengan mudah.",
                fontSize = 13.sp,
                color = Color(0xFFE3F2FD)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 22.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Menu Utama",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )

                Surface(
                    color = Color(0xFFE3F2FD),
                    shape = RoundedCornerShape(50.dp)
                ) {
                    Text(
                        text = "Dashboard",
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 7.dp
                        ),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1565C0)
                    )
                }
            }

            HomeMenuCard(
                icon = Icons.Default.PersonAdd,
                iconBgColor = Color(0xFF1976D2),
                label = "Pendaftaran Anggota",
                onClick = onNavigateToRegister
            )

            HomeMenuCard(
                icon = Icons.Default.Group,
                iconBgColor = Color(0xFF1976D2),
                label = "Daftar Anggota",
                onClick = onNavigateToManagement
            )

            HomeMenuCard(
                icon = Icons.Default.Search,
                iconBgColor = Color(0xFF1976D2),
                label = "Pencarian Data",
                onClick = onNavigateToManagement
            )

            HomeMenuCard(
                icon = Icons.Default.Settings,
                iconBgColor = Color(0xFF1976D2),
                label = "Kelola Data",
                onClick = onNavigateToManagement
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 1.dp
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                Color(0xFFE3F2FD),
                                RoundedCornerShape(14.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Group,
                            contentDescription = null,
                            tint = Color(0xFF1976D2),
                            modifier = Modifier.size(27.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Status Sistem Aktif",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color(0xFF1E293B)
                        )

                        Spacer(modifier = Modifier.height(3.dp))

                        Text(
                            text = "Total anggota terdaftar",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B)
                        )
                    }

                    Text(
                        text = totalMembers.toString(),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1976D2)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "Orang",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }
        }
    }
}

@Composable
fun HomeMenuCard(
    icon: ImageVector,
    iconBgColor: Color,
    label: String,
    onClick: () -> Unit
) {
    val subtitle = when (label) {
        "Pendaftaran Anggota" -> "Daftarkan anggota baru"
        "Daftar Anggota" -> "Lihat data anggota terdaftar"
        "Pencarian Data" -> "Temukan data dengan cepat"
        "Kelola Data" -> "Edit dan hapus data anggota"
        else -> "Kelola data perpustakaan"
    }

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 82.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(
                        Color(0xFFE3F2FD),
                        RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconBgColor,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = label,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Color(0xFF64748B)
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Buka $label",
                tint = Color(0xFF94A3B8),
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    existingMember: Member? = null,
    onBack: () -> Unit,
    onSubmit: (Member) -> Unit
) {
    val context = LocalContext.current

    var name by remember {
        mutableStateOf(existingMember?.name ?: "")
    }
    var nimNip by remember {
        mutableStateOf(existingMember?.nimNip ?: "")
    }
    var selectedProdi by remember {
        mutableStateOf(existingMember?.prodi ?: "Informatika")
    }
    var email by remember {
        mutableStateOf(existingMember?.email ?: "")
    }
    var phone by remember {
        mutableStateOf(existingMember?.phone ?: "")
    }
    var address by remember {
        mutableStateOf(existingMember?.address ?: "")
    }
    var selectedMemberType by remember {
        mutableStateOf(existingMember?.memberType ?: "Mahasiswa")
    }

    var nameError by remember { mutableStateOf<String?>(null) }
    var nimError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }

    val prodiList = listOf(
        "Informatika",
        "Sistem Informasi",
        "Teknik Komputer",
        "Manajemen",
        "Dosen/Staf"
    )

    val memberTypeList = listOf(
        "Mahasiswa",
        "Dosen",
        "Tenaga Kependidikan",
        "Umum"
    )

    var prodiExpanded by remember { mutableStateOf(false) }
    var memberTypeExpanded by remember { mutableStateOf(false) }

    fun validateInputs(): Boolean {
        var isValid = true

        if (name.isBlank()) {
            nameError = "Nama lengkap wajib diisi"
            isValid = false
        } else {
            nameError = null
        }

        if (nimNip.isBlank()) {
            nimError = "Nomor induk (NIM/NIP) wajib diisi"
            isValid = false
        } else if (!nimNip.all { it.isDigit() }) {
            nimError = "Nomor induk harus berupa angka"
            isValid = false
        } else {
            nimError = null
        }

        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

        if (email.isBlank()) {
            emailError = "Email wajib diisi"
            isValid = false
        } else if (!email.matches(emailPattern.toRegex())) {
            emailError = "Format email tidak valid (contoh: user@domain.com)"
            isValid = false
        } else {
            emailError = null
        }

        if (phone.isBlank()) {
            phoneError = "Nomor telepon wajib diisi"
            isValid = false
        } else {
            phoneError = null
        }

        return isValid
    }

    Scaffold(
        containerColor = Color(0xFFF4F7FC),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (existingMember == null) {
                            "Formulir Pendaftaran"
                        } else {
                            "Edit Anggota"
                        },
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color(0xFF1E293B),
                    navigationIconContentColor = Color(0xFF1976D2)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nama Lengkap *") },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null)
                },
                isError = nameError != null,
                supportingText = {
                    nameError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF1976D2),
                    focusedLabelColor = Color(0xFF1976D2)
                )
            )

            OutlinedTextField(
                value = nimNip,
                onValueChange = { nimNip = it },
                label = { Text("NIM / NIP *") },
                leadingIcon = {
                    Icon(Icons.Default.Badge, contentDescription = null)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                isError = nimError != null,
                supportingText = {
                    nimError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF1976D2),
                    focusedLabelColor = Color(0xFF1976D2)
                )
            )

            ExposedDropdownMenuBox(
                expanded = prodiExpanded,
                onExpandedChange = {
                    prodiExpanded = !prodiExpanded
                }
            ) {
                OutlinedTextField(
                    value = selectedProdi,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Program Studi / Unit") },
                    leadingIcon = {
                        Icon(Icons.Default.School, contentDescription = null)
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = prodiExpanded
                        )
                    },
                    modifier = Modifier
                        .menuAnchor(
                            ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                            true
                        )
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF1976D2),
                        focusedLabelColor = Color(0xFF1976D2)
                    )
                )

                ExposedDropdownMenu(
                    expanded = prodiExpanded,
                    onDismissRequest = {
                        prodiExpanded = false
                    }
                ) {
                    prodiList.forEach { prodi ->
                        DropdownMenuItem(
                            text = { Text(prodi) },
                            onClick = {
                                selectedProdi = prodi
                                prodiExpanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email *") },
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = null)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                ),
                isError = emailError != null,
                supportingText = {
                    emailError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF1976D2),
                    focusedLabelColor = Color(0xFF1976D2)
                )
            )

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Nomor Telepon *") },
                leadingIcon = {
                    Icon(Icons.Default.Phone, contentDescription = null)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone
                ),
                isError = phoneError != null,
                supportingText = {
                    phoneError?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF1976D2),
                    focusedLabelColor = Color(0xFF1976D2)
                )
            )

            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Alamat") },
                leadingIcon = {
                    Icon(Icons.Default.Home, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF1976D2),
                    focusedLabelColor = Color(0xFF1976D2)
                )
            )

            ExposedDropdownMenuBox(
                expanded = memberTypeExpanded,
                onExpandedChange = {
                    memberTypeExpanded = !memberTypeExpanded
                }
            ) {
                OutlinedTextField(
                    value = selectedMemberType,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Jenis Keanggotaan") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.CardMembership,
                            contentDescription = null
                        )
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = memberTypeExpanded
                        )
                    },
                    modifier = Modifier
                        .menuAnchor(
                            ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                            true
                        )
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF1976D2),
                        focusedLabelColor = Color(0xFF1976D2)
                    )
                )

                ExposedDropdownMenu(
                    expanded = memberTypeExpanded,
                    onDismissRequest = {
                        memberTypeExpanded = false
                    }
                ) {
                    memberTypeList.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type) },
                            onClick = {
                                selectedMemberType = type
                                memberTypeExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    if (validateInputs()) {
                        val member = Member(
                            id = existingMember?.id
                                ?: UUID.randomUUID().toString(),
                            name = name,
                            nimNip = nimNip,
                            prodi = selectedProdi,
                            email = email,
                            phone = phone,
                            address = address,
                            memberType = selectedMemberType
                        )

                        onSubmit(member)

                        Toast.makeText(
                            context,
                            "Data Berhasil Disimpan!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            context,
                            "Mohon periksa validasi form kembali",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1976D2)
                )
            ) {
                Text(
                    text = if (existingMember == null) {
                        "Daftar"
                    } else {
                        "Simpan Perubahan"
                    },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagementScreen(
    memberList: List<Member>,
    onBack: () -> Unit,
    onAddMember: () -> Unit,
    onEditMember: (Member) -> Unit,
    onDeleteMember: (Member) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredList = memberList.filter {
        it.name.contains(searchQuery, ignoreCase = true) ||
                it.nimNip.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        containerColor = Color(0xFFF4F7FC),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Manajemen Anggota",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color(0xFF1E293B),
                    navigationIconContentColor = Color(0xFF1976D2)
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddMember,
                containerColor = Color(0xFF1976D2),
                contentColor = Color.White
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Tambah Anggota"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = {
                    Text("Cari nama atau nomor induk...")
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        tint = Color(0xFF1976D2)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { searchQuery = "" }
                        ) {
                            Icon(
                                Icons.Default.Clear,
                                contentDescription = "Hapus pencarian"
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF1976D2),
                    focusedLabelColor = Color(0xFF1976D2),
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Daftar Anggota (${filteredList.size})",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF1E293B)
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (filteredList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.SearchOff,
                            contentDescription = null,
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(48.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Data tidak ditemukan",
                            color = Color(0xFF64748B)
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(
                        filteredList,
                        key = { it.id }
                    ) { member ->
                        MemberItemCard(
                            member = member,
                            onEdit = {
                                onEditMember(member)
                            },
                            onDelete = {
                                onDeleteMember(member)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MemberItemCard(
    member: Member,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = member.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF1E293B)
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "Nomor Induk: ${member.nimNip}",
                    fontSize = 13.sp,
                    color = Color(0xFF64748B)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${member.memberType} • ${member.prodi}",
                    fontSize = 12.sp,
                    color = Color(0xFF1976D2)
                )
            }

            Row {
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color(0xFF1976D2)
                    )
                }

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Hapus",
                        tint = Color(0xFFE53935)
                    )
                }
            }
        }
    }
}