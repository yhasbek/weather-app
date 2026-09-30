const countrySelect = document.getElementById("country");
const citySelect = document.getElementById("city");
const message = document.getElementById("message");
const result = document.getElementById("result");

// --- Yardımcılar -------------------------------------------------------

async function fetchJson(url) {
    const response = await fetch(url);
    if (!response.ok) {
        throw new Error(`İstek başarısız oldu (HTTP ${response.status})`);
    }
    return response.json();
}

function showMessage(text) {
    message.textContent = text;
    message.hidden = false;
}

function hideMessage() {
    message.hidden = true;
}

function createElement(tag, className, text) {
    const element = document.createElement(tag);
    element.className = className;
    element.textContent = text;
    return element;
}

function formatDate(isoDate) {
    const date = new Date(`${isoDate}T00:00:00`);
    return date.toLocaleDateString("tr-TR", { weekday: "long", day: "numeric", month: "short" });
}

function formatTemperature(value) {
    return value == null ? "-" : `${Math.round(value)}°`;
}

function iconFor(code) {
    if (code === 0) return "☀️";
    if (code === 1 || code === 2) return "🌤️";
    if (code === 3) return "☁️";
    if (code === 45 || code === 48) return "🌫️";
    if (code >= 51 && code <= 67) return "🌧️";
    if ((code >= 71 && code <= 77) || code === 85 || code === 86) return "❄️";
    if (code >= 80 && code <= 82) return "🌦️";
    if (code >= 95) return "⛈️";
    return "🌡️";
}

// --- Perde 1: Ülkeleri yükle ------------------------------------------

async function loadCountries() {
    try {
        const countries = await fetchJson("/api/countries");
        countries.forEach(country => {
            countrySelect.add(new Option(country.name, country.code));
        });
    } catch (error) {
        showMessage(`Ülkeler yüklenemedi. ${error.message}`);
    }
}

// --- Perde 2: Ülke seçilince şehirleri yükle --------------------------

function resetCities(placeholder) {
    citySelect.replaceChildren(new Option(placeholder, ""));
    citySelect.disabled = true;
}

countrySelect.addEventListener("change", async () => {
    result.replaceChildren();
    hideMessage();

    const countryCode = countrySelect.value;
    if (!countryCode) {
        resetCities("Önce ülke seçin");
        return;
    }

    resetCities("Şehirler yükleniyor...");
    try {
        const cities = await fetchJson(`/api/cities?countryCode=${encodeURIComponent(countryCode)}`);
        resetCities("Şehir seçin");
        cities.forEach(city => {
            citySelect.add(new Option(city.name, city.id));
        });
        citySelect.disabled = false;
    } catch (error) {
        resetCities("Şehirler yüklenemedi");
        showMessage(`Şehirler yüklenemedi. ${error.message}`);
    }
});

// --- Perde 3: Şehir seçilince hava durumunu yükle ---------------------

citySelect.addEventListener("change", async () => {
    result.replaceChildren();

    const cityId = citySelect.value;
    if (!cityId) {
        hideMessage();
        return;
    }

    showMessage("Hava durumu yükleniyor...");
    try {
        const forecast = await fetchJson(`/api/weather/${cityId}`);
        renderForecast(forecast);
        hideMessage();
    } catch (error) {
        showMessage(`Hava durumu alınamadı. ${error.message}`);
    }
});

function renderForecast(forecast) {
    const title = createElement("h2", "", `${forecast.cityName} · 7 günlük tahmin`);
    const grid = createElement("div", "grid", "");

    forecast.days.forEach(day => {
        const card = createElement("article", "card", "");
        card.append(
            createElement("p", "day", formatDate(day.date)),
            createElement("p", "icon", iconFor(day.weatherCode)),
            createElement("p", "desc", day.description),
            createElement("p", "temp",
                `${formatTemperature(day.maxTemperature)} / ${formatTemperature(day.minTemperature)}`),
            createElement("p", "rain", `Yağış olasılığı: %${day.precipitationProbability ?? "-"}`)
        );
        grid.append(card);
    });

    result.replaceChildren(title, grid);
}

// --- Başlangıç ---------------------------------------------------------

loadCountries();