# Palačinke

> Small IOT solution for watering house plants. Source code for integrated system
> containing implementation for a physical sensor and actuator through python on FiPy, simulated virtual sensors, Home
> Assistant configuration file and Angular Spring Boot web application.
> 
> GitHub Repository: ``https://github.com/pasalk/Palacinke``

## FiPy

**Priprema za rad FiPya:**
> Instalacija Visual Studio Code-a
>
>Instalacija PyMakr ekstenzije

**Priprema na FiPyu:**
> 1. Spojiti DHT11 senzor za vlagu i temperaturu na pin 3
>
>2. Spojiti otpornik od 4.7 kΩ između VCC pina i Signal pina na senzoru
>
>3. Spojiti RGB LED diodu: crvenu na pin 12, plavu na pin 11 i zelenu na pin 10
>
>5. Spojiti 3 otpornika od 330 Ω između pina na diodi i pina na fipyu za svaku boju


**Pokretanje FiPya:**
> 1. Otvorite projekt koristeći PyMakr ekstenziju
>
>2. Ukopčajte FiPy uređaj u računalo
>
>3. Kada se uređaj pronađe dodajte uređaj u projekt
>
>4. Povežite ekstenziju sa FiPyem (gumb sa slikom munje)
>
>5. Otvorite terminal koristeći ekstenziju
>
>6. Kod postavite na uređaj (upload gumb)
>
>7. Postavite se u skriptu fipy.py -> desni klik -> pymakr -> run file or selection on device

**Napomena:**

- Provjerite da ste sva polja povezane s konekcijom zamjenili vlastitim vrijednostima (autentifikacijski token, URL Home
  Assistanta, WiFi SSID, Wifi šifru)
    - Token se generira kroz sučelje Home Assistanta
- Ako imate problema sa uređajem pokrenite Safe boot opciju

## Virtualni Senzori

**Priprema za rad s Virtualnim Senzorima:**
> Instalacija Python-a

**Pokretanje Home Assistant-a:**
> 1. Otvorite terminal
>
>2. Instalacija potrebnih Python biblioteka:
>
>   ``pip install requests flask colorama``
>3. Pokretanje virtualnih senzora
>
>   ``python virtual_senzor_2.py``
> 
>   ``python virtual_senzor_3.py``
> 
>   ``python virtualni_senzor_4.py``

**Napomena:**

- Provjerite da ste sva polja povezana s konekcijom zamjenili vlastitim vrijednostima (autentifikacijski token, URL Home
  Assistanta)
    - Token se generira kroz sučelje Home Assistanta
- Ako Home Assistant nije dostupan, može se pokrenut json_taker skriptu koja će simulirati prijem podataka:
    - ``` python json_taker.py```

## Home Assistant

**Priprema za rad Home Assistant-a:**
> Instalacija Docker-a

**Pokretanje Home Assistant-a:**
> 1. Otvorite terminal
>
>2. Povucite Docker sliku
>
>   ``docker pull ghcr.io/home-assistant/home-assistant:stable``
>3. Pokrenite kontejner
>
>   ``docker run -d \
    --name home-assistant \
    --restart=unless-stopped \
    --network=home-assistant \
    -v /path/to/your/config:/config \
    -e TZ=YOUR_TIME_ZONE \
    -p 8123:8123 \
    ghcr.io/home-assistant/home-assistant:stable
    ``
> 
>4. Otvorite Home Assistant u pregledniku i pratite ostatak uputa
> 
>5. U direktoriju gdje ste namjestili Home Assistant zamjenite ``configuration.yaml`` dokument s dokumentom u ovom
    projektu

**Napomena:**

- Na početku kreirajte Docker mrežu radi bolje komunikacije među kontejnerima
    - ``docker network create home-assistant``
- Prilikom pokretanja kontejnera zamjenite ``/path/to/your/config`` putanjom do odabranog direktorija
  i ``YOUR_TIME_ZONE`` svojom vremenskom zonom
- Potrebno je kreirati vlastite Bearer tokene za autentifikaciju vanjskih aplikacija
    - u web pregledniku na kartici ``Username -> Security -> Long-lived access tokens -> Create token``
- Provjerite da ste sva polja povezane s konekcijom zamjenili vlastitim vrijednostima (URL FiPy uređaja, URL Spring Boot
  aplikacije)
- Predodređeni port za pokretanje Home Assistant aplikacije je **8123**

## Spring Boot
**Priprema za rad Spring Boot aplikacije:**
> Instalacija JDK v.17
>
> Instalacija Docker-a
> 
> Instalacija Maven-a

**Pokretanje Spring Boot aplikacije:**
> 1. Kreirajte bazu podataka i mongo-express pokretanjem docker_compose.yaml datoteke
>
> 2. Kreirajte izvršnu datoteku projekta
> 
>   ``mvn clean compile``
> 
> 3. Pokrenite aplikaciju
>
>   ``mvn spring-boot:run``

**Napomena:**
- Provjerite da ste sva polja povezane s konekcijom zamjenili vlastitim vrijednostima (autentifikacijski token, URL Home
  Assistanta, URL Angular aplikacije)
    - Token se generira kroz sučelje Home Assistanta
- Predodređeni port za pokretanje Spring Boot aplikacije je **8080**
- Predodređeni port za MongoDB bazu je **27027** i za mongo-express sučelje je **8081**

## Angular

**Priprema za rad i pokretanje Angular aplikacije:**
> 1. Instalacija Node.js-a i npm-a
>
>    ``https://nodejs.org/en/download/package-manager``
>
> 2. Otvorite terminal
> 3. Instalacija Angular CLI
>
>   ``npm install -g @angular/cli``
>
> 4. Pozicionirajte se u direktorij projekta:
>
>   ``cd path/to/your/angular/project``
> 
> 5. Instalirajte potrebne pakete
>
>   ``npm install``
> 
> 6. Pokrenite aplikaciju
>
>   ``ng serve``

**Napomena:**
- Provjerite da ste sva polja povezane s konekcijom zamjenili vlastitim vrijednostima (URL Spring Boot aplikacije)
- Predodređeni port za pokretanje Angular aplikacije je **4200**
