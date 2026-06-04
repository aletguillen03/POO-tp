# Cambios en la Interfaz Grafica (UI)

Fecha: 2026-06-04

## Resumen

Se reescribio por completo la interfaz grafica Swing del juego **Carrera de
Caballos** para dejar una version **basica y simple**, usando solo componentes
estandar de Swing y el look & feel por defecto.

**Solo se modifico el archivo de la vista. NO se toco nada del backend.**

- Archivo modificado: `CarreraCaballo/src/vista/VistaCarreras.java`
- Archivos del backend (modelo, controlador, dto, dao, service, util, Main):
  **sin cambios**.

La vista sigue usando exactamente la misma API del `ControladorCarrera`, por lo
que el backend no necesito ningun ajuste.

---

## Que se saco (UI anterior)

La version anterior era una UI "decorada":

- Tema oscuro con colores personalizados en cada panel y boton.
- Botones con colores de fondo, botones secundarios sin borde, etc.
- Seleccion de caballo mediante **tarjetas** (`JPanel`) dibujadas a mano, con
  circulos pintados con `Graphics2D`, efectos de hover (`MouseListener`) y
  bordes que cambiaban al pasar el mouse.
- La carrera se mostraba con una clase interna `PistaPanel` que **dibujaba la
  pista a mano** (carriles, lineas de meta punteadas, caballos como
  rectangulos redondeados con sombra y brillo, distancia en metros, etc.) usando
  `paintComponent` y `Graphics2D`.
- Detalle de historial en un `JTextArea` con borde titulado.
- Metodos auxiliares de estilo: `labelBlanco`, `boton`, `botonSecundario`,
  `colorDesdeCadena`, y la clase interna `PistaPanel`.

## Que quedo (UI nueva, basica)

Misma estructura de 6 pantallas, intercambiadas con `CardLayout`:
Login, Registro, Seleccion de caballo, Carrera, Resultado e Historial.

Cambios concretos por pantalla:

1. **Login y Registro**
   - Layout `GridBagLayout` simple con `JLabel`, `JTextField`,
     `JPasswordField` y `JButton` estandar (sin colores de fondo).
   - Los mensajes de error siguen mostrandose en rojo (`Color.RED`).

2. **Seleccion de caballo**
   - Se reemplazaron las tarjetas dibujadas a mano por un **`JList`** simple
     dentro de un `JScrollPane`.
   - Cada caballo se muestra como una linea de texto monoespaciado con su
     nombre, tipo (Veloz / Lento / Estandar / Random) y velocidad base.
   - Al entrar se selecciona el primer caballo por defecto.
   - Botones "Iniciar carrera" y "Ver historial".

3. **Carrera**
   - Se elimino el panel de dibujo `PistaPanel`.
   - Ahora cada caballo se representa con una **`JProgressBar`** (rango
     `0..200`) acompanada de un `JLabel` con su nombre. El caballo del jugador
     se marca con el texto "(TU CABALLO)".
   - Las barras se crean dinamicamente al iniciar cada carrera (`prepararBarras`).
   - El `Timer` de 500 ms sigue avanzando turnos y ahora actualiza el valor de
     cada barra. La barra muestra los metros recorridos como texto.

4. **Resultado**
   - Mismos `JLabel` (ganador, puntos, puntaje total) y `JTextArea` con las
     posiciones finales, pero sin colores personalizados.

5. **Historial**
   - `JTable` estandar (sin colores ni cabecera personalizada).
   - Se quito el `JTextArea` de detalle por seleccion de fila. Si no hay
     carreras, se avisa con un `JOptionPane`.

---

## Correccion / robustez aplicada

En la version anterior la pista se redibujaba completa en cada turno a partir de
`estado.caballos`, asi que el orden no importaba. En la version nueva, en cambio,
las etiquetas de cada barra se crean una sola vez al iniciar la carrera.

Para evitar un posible **desajuste entre la etiqueta y la barra** si el orden de
`getCaballosDisponibles()` no coincidiera con el de `avanzarTurno()`, las barras
se guardan en un `Map<String, JProgressBar>` (clave = nombre del caballo) y se
actualizan **buscando por nombre**, no por indice. Asi cada barra siempre se
corresponde con su caballo aunque el backend cambie el orden de las listas.

---

## Verificacion

- `mvn -o clean compile` → **BUILD SUCCESS** (26 archivos compilados, sin
  warnings de compilacion propios del cambio).
- No quedaron referencias colgadas a los simbolos eliminados (`PistaPanel`,
  `colorDesdeCadena`, `boton`, etc.); la compilacion lo confirma.
- No se pudo ejecutar la app de forma automatica en este entorno porque
  requiere la base de datos MySQL/Hibernate configurada (se ejecuta desde
  IntelliJ IDEA). La logica de la vista se verifico contra la API del
  `ControladorCarrera`, que no cambio.
