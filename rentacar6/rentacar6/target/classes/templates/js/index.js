// Kiralama modalında tarih seçimi ve fiyat hesaplama fonksiyonu

// Modal için global değişkenler
let selectedCar = null;

// Modal içeriğini yükleme fonksiyonu
function showRentModal(car) {
    selectedCar = car;

    // Modal içeriğini doldur
    document.getElementById("modal-car-title").textContent = `${car.brand} ${car.model}`;
    document.getElementById("modal-car-description").textContent = `Year: ${car.year}, Color: ${car.color}`;
    document.getElementById("modal-car-price").textContent = `$${car.dailyPrice}`;

    // Modal'ı göster
    const modal = document.getElementById("car-details-modal");
    modal.style.display = "flex";
}

// Modal kapatma işlevi
function closeModal() {
    const modal = document.getElementById("car-details-modal");
    modal.style.display = "none";
}

// Toplam fiyat hesaplama fonksiyonu
function calculateTotalPrice() {
    const rentDate = new Date(document.getElementById("rent-date").value);
    const returnDate = new Date(document.getElementById("return-date").value);

    if (rentDate && returnDate && rentDate < returnDate) {
        const days = Math.ceil((returnDate - rentDate) / (1000 * 60 * 60 * 24));
        const totalPrice = days * selectedCar.dailyPrice;
        document.getElementById("total-price").textContent = `$${totalPrice}`;
    } else {
        document.getElementById("total-price").textContent = "Invalid dates";
    }
}

// Kiralama formunu gönderme işlemi
async function submitRentalForm() {
    const rentDate = document.getElementById("rent-date").value;
    const returnDate = document.getElementById("return-date").value;

    try {
        await apiFetch('/api/orders', 'POST', {
            carId: selectedCar.id,
            rentDate,
            returnDate
        });

        alert("Order placed successfully!");
        location.reload();
    } catch (error) {
        console.error("Error placing order:", error);
        alert("Failed to place order. Please try again.");
    }
}

// Modal içinde tarih seçimi değiştikçe toplam fiyatı hesapla
function attachDateChangeListeners() {
    document.getElementById("rent-date").addEventListener("change", calculateTotalPrice);
    document.getElementById("return-date").addEventListener("change", calculateTotalPrice);
}

// Modal kiralama formu gönderim işlemi
function attachFormSubmissionListener() {
    document.getElementById("modal-rent-button").addEventListener("click", submitRentalForm);
}

// Araçları yükleyen fonksiyon
async function loadCars() {
    const carList = document.getElementById("car-list");

    try {
        const response = await fetch('/api/cars');
        const cars = await response.json();

        carList.innerHTML = cars.map(car => `
            <div class="car-card">
                <img 
                    src="${car.imageUrl || '/uploads/cars/default.jpg'}" 
                    alt="${car.brand} ${car.model}" 
                    class="car-image"
                    onerror="this.src='/uploads/cars/default.jpg';"
                >
                <div class="car-info">
                    <h3 class="car-title">${car.brand} ${car.model}</h3>
                    <p class="car-year">${car.year} - ${car.color}</p>
                    <p class="car-price">Daily Price: $${car.dailyPrice}</p>
                    <button class="btn rent-btn" data-id="${car.id}">Rent Now</button>
                </div>
            </div>
        `).join('');

        attachRentButtons();
    } catch (error) {
        console.error("Error loading cars:", error);
        carList.innerHTML = '<p class="error-message">Failed to load cars. Please try again later.</p>';
    }
}

// Rent butonlarına olay dinleyicisi ekler
function attachRentButtons() {
    document.querySelectorAll(".rent-btn").forEach(button => {
        button.addEventListener("click", async (e) => {
            const carId = e.target.dataset.id;
            try {
                const response = await fetch(`/api/cars/${carId}`);
                const car = await response.json();
                showRentModal(car);
            } catch (error) {
                console.error("Error loading car details:", error);
                alert("Failed to load car details. Please try again.");
            }
        });
    });
}

// Fonksiyonları başlat
loadCars();
attachDateChangeListeners();
attachFormSubmissionListener();
