# Sistema de Gestión de Clínica Veterinaria «Patitas»

Trabajo Práctico Obligatorio de Programación Orientada a Objetos.

## 👥 Integrantes
* Agustin Suarez
* Octavio Pena
* Palma Santiago

## 📌 Propuesta Elegida
* **Propuesta B:** Clínica Veterinaria

## 📝 Descripción
Sistema por consola para la clínica veterinaria «Patitas». Permite registrar dueños y mascotas, manejar los turnos de los veterinarios, registrar consultas con su historia clínica y vacunas, controlar el stock de medicamentos y facturar las atenciones. Los datos se guardan en archivos de texto, así que se conservan entre una ejecución y otra.

Al iniciar se elige con qué rol entrar:
* **Recepción:** registrar dueños y mascotas, dar, reprogramar y cancelar turnos, registrar asistencias, urgencias y facturar.
* **Veterinario:** atender una mascota (consulta, diagnóstico, tratamientos y vacunas) y ver su historia clínica.
* **Administrador:** ver reportes (stock bajo, vacunas por vencer, turnos de cada veterinario) y cargar lotes, insumos, proveedores y vacunas.

## ⚙️ Requisitos
* Java 17 o superior.
* JUnit 5 para correr las pruebas unitarias.
* IntelliJ IDEA (recomendado).

## ▶️ Instalación y Ejecución
1. Clonar el repositorio y abrir la carpeta `TP2_POO` en IntelliJ (**File → Open**).
2. Si la carpeta `src` no aparece en azul: clic derecho sobre `src` → **Mark Directory as → Sources Root**.
3. En **File → Project Structure → Project**, elegir un SDK de Java 17 o superior.
4. Abrir `src/Main.java` y ejecutarlo.

El programa lee la carpeta `datos/` desde la raíz del proyecto. Si arranca sin datos, revisar en **Run → Edit Configurations** que el *Working directory* sea la carpeta `TP2_POO`.

También se puede ejecutar desde la terminal, parado en `TP2_POO`:

```bash
javac -encoding UTF-8 -d out $(find src -name "*.java")
java -cp out Main
```

## 🧪 Pruebas Unitarias
1. Clic derecho sobre la carpeta `test` → **Mark Directory as → Test Sources Root**.
2. Si `@Test` aparece en rojo: poner el cursor encima → **Alt + Enter → Add 'JUnit5' to classpath**.
3. Clic derecho sobre `test` → **Run 'All Tests'**.

## 🗂️ Datos de Prueba
La carpeta `datos/` ya trae datos cargados para probar el sistema:

* **Veterinaria:** Laura Gómez, documento `20111222` (lunes a viernes de 9 a 19, sábados de 9 a 13).
* **Veterinario:** Martín Ruiz, documento `20333444` (lunes y miércoles de 14 a 19).
* **Dueña:** Ana Paz, DNI `40111222`, tiene plan de salud. Mascotas: Toby (perro) y Michi (gata de pocos meses).
* **Dueño:** Juan Pérez, DNI `40333444`, ya tiene 2 ausencias seguidas. Mascotas: Copito (conejo) y Pepe (canario).
* **Insumos:** `MED001` Amoxicilina (con stock), `MED002` Meloxicam (su único lote está vencido), `MED003` Desparasitante (por debajo del mínimo).
* **Vacunas:** Antirrábica (perros y gatos), Séxtuple (perros), Triple Felina (gatos), Mixomatosis (conejos).

Algunas cosas para probar:
* Facturar una consulta a Ana pagando en efectivo: se aplica el 15 % del plan de salud y no se suma el 10 % del efectivo.
* Entrar como Laura y aplicarle la Antirrábica a Michi: la rechaza porque no tiene la edad mínima.
* Indicar Meloxicam en una consulta e intentar finalizarla: no deja porque no hay stock disponible.
* Registrar una ausencia más de Juan e intentar darle otro turno: pide seña.
* Como administrador, ver los productos con stock bajo: aparecen Meloxicam y Desparasitante.

Para volver a los datos originales, reemplazar la carpeta `datos/` por la del repositorio.

