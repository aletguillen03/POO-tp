package vista;

import controlador.ControladorCaballo;
import controlador.ControladorCarrera;
import controlador.ControladorJugador;
import dto.CaballoDTO;
import dto.EstadoCarreraDTO;
import dto.HistorialDTO;
import dto.JugadorDTO;
import dto.ResultadoDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class VistaCarreras extends JFrame {

    private final ControladorJugador controladorJugador;
    private final ControladorCaballo controladorCaballo;
    private final ControladorCarrera controladorCarrera;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel panelPrincipal = new JPanel(cardLayout);

    private static final String PANTALLA_LOGIN     = "LOGIN";
    private static final String PANTALLA_REGISTRO  = "REGISTRO";
    private static final String PANTALLA_CABALLOS  = "CABALLOS";
    private static final String PANTALLA_CARRERA   = "CARRERA";
    private static final String PANTALLA_RESULTADO = "RESULTADO";
    private static final String PANTALLA_HISTORIAL = "HISTORIAL";

    private static final int DISTANCIA_CARRERA = 200;

    // --- Pantalla login ---
    private JTextField loginMailField;
    private JPasswordField loginPassField;
    private JLabel loginMensaje;

    // --- Pantalla registro ---
    private JTextField regNombreField;
    private JTextField regMailField;
    private JPasswordField regPassField;
    private JLabel regMensaje;

    // --- Sesion activa ---
    private JugadorDTO jugadorActual;

    // --- Pantalla seleccion de caballos ---
    private JLabel labelBienvenida;
    private DefaultListModel<String> modeloCaballos;
    private JList<String> listaCaballos;
    private List<CaballoDTO> caballosDisponibles = new ArrayList<>();

    // --- Pantalla carrera ---
    private JLabel labelTurno;
    private JPanel panelBarras;
    private final Map<String, JProgressBar> barrasPorCaballo = new LinkedHashMap<>();
    private final Map<String, JLabel> etiquetasPorCaballo = new LinkedHashMap<>();
    private String caballoSeleccionado = null;
    private Timer timerCarrera;

    // --- Pantalla resultado ---
    private JLabel labelGanador;
    private JLabel labelPuntaje;
    private JLabel labelPuntajeTotal;
    private JTextArea areaResultado;

    // --- Pantalla historial ---
    private DefaultTableModel historialTableModel;
    private JTable historialTable;
    private List<HistorialDTO> historialData = new ArrayList<>();

    public VistaCarreras() {
        controladorJugador = new ControladorJugador();
        controladorCaballo = new ControladorCaballo();
        controladorCarrera = new ControladorCarrera();

        setTitle("Carrera de Caballos");
        setSize(640, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        panelPrincipal.add(crearPantallaLogin(),     PANTALLA_LOGIN);
        panelPrincipal.add(crearPantallaRegistro(),  PANTALLA_REGISTRO);
        panelPrincipal.add(crearPantallaCaballos(),  PANTALLA_CABALLOS);
        panelPrincipal.add(crearPantallaCarrera(),   PANTALLA_CARRERA);
        panelPrincipal.add(crearPantallaResultado(), PANTALLA_RESULTADO);
        panelPrincipal.add(crearPantallaHistorial(), PANTALLA_HISTORIAL);

        add(panelPrincipal);
        mostrar(PANTALLA_LOGIN);
    }

    private void mostrar(String pantalla) {
        cardLayout.show(panelPrincipal, pantalla);
    }

    // Pantalla: Login
    private JPanel crearPantallaLogin() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 6, 6, 6);
        g.fill = GridBagConstraints.HORIZONTAL;

        JLabel titulo = new JLabel("Carrera de Caballos", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
        panel.add(titulo, g);

        g.gridwidth = 1;
        g.gridx = 0; g.gridy = 1; panel.add(new JLabel("Mail:"), g);
        loginMailField = new JTextField(18);
        g.gridx = 1; panel.add(loginMailField, g);

        g.gridx = 0; g.gridy = 2; panel.add(new JLabel("Contrasena:"), g);
        loginPassField = new JPasswordField(18);
        g.gridx = 1; panel.add(loginPassField, g);

        loginMensaje = new JLabel(" ", SwingConstants.CENTER);
        loginMensaje.setForeground(Color.RED);
        g.gridx = 0; g.gridy = 3; g.gridwidth = 2;
        panel.add(loginMensaje, g);

        JButton btnLogin = new JButton("Ingresar");
        btnLogin.addActionListener(e -> accionLogin());
        g.gridy = 4; panel.add(btnLogin, g);

        JButton btnIrRegistro = new JButton("Crear una cuenta nueva");
        btnIrRegistro.addActionListener(e -> mostrar(PANTALLA_REGISTRO));
        g.gridy = 5; panel.add(btnIrRegistro, g);

        return panel;
    }

    private void accionLogin() {
        String mail = loginMailField.getText().trim();
        String pass = new String(loginPassField.getPassword());
        if (mail.isEmpty() || pass.isEmpty()) {
            loginMensaje.setText("Completa todos los campos.");
            return;
        }
        JugadorDTO dto = controladorJugador.login(mail, pass);
        if (dto != null) {
            jugadorActual = dto;
            loginMensaje.setText(" ");
            actualizarBienvenida(jugadorActual.nombre, jugadorActual.puntaje);
            cargarCaballos();
            mostrar(PANTALLA_CABALLOS);
        } else {
            loginMensaje.setText("Mail o contrasena incorrectos.");
        }
    }

    // Pantalla: Registro
    private JPanel crearPantallaRegistro() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 6, 6, 6);
        g.fill = GridBagConstraints.HORIZONTAL;

        JLabel titulo = new JLabel("Crear cuenta", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
        panel.add(titulo, g);

        g.gridwidth = 1;
        g.gridx = 0; g.gridy = 1; panel.add(new JLabel("Nombre:"), g);
        regNombreField = new JTextField(18);
        g.gridx = 1; panel.add(regNombreField, g);

        g.gridx = 0; g.gridy = 2; panel.add(new JLabel("Mail:"), g);
        regMailField = new JTextField(18);
        g.gridx = 1; panel.add(regMailField, g);

        g.gridx = 0; g.gridy = 3; panel.add(new JLabel("Contrasena:"), g);
        regPassField = new JPasswordField(18);
        g.gridx = 1; panel.add(regPassField, g);

        regMensaje = new JLabel(" ", SwingConstants.CENTER);
        regMensaje.setForeground(Color.RED);
        g.gridx = 0; g.gridy = 4; g.gridwidth = 2;
        panel.add(regMensaje, g);

        JButton btnRegistrar = new JButton("Registrarse");
        btnRegistrar.addActionListener(e -> accionRegistro());
        g.gridy = 5; panel.add(btnRegistrar, g);

        JButton btnVolver = new JButton("Volver al login");
        btnVolver.addActionListener(e -> mostrar(PANTALLA_LOGIN));
        g.gridy = 6; panel.add(btnVolver, g);

        return panel;
    }

    private void accionRegistro() {
        String nombre = regNombreField.getText().trim();
        String mail   = regMailField.getText().trim();
        String pass   = new String(regPassField.getPassword());
        if (nombre.isEmpty() || mail.isEmpty() || pass.isEmpty()) {
            regMensaje.setText("Completa todos los campos.");
            return;
        }
        JugadorDTO dto = controladorJugador.crearJugador(nombre, mail, pass);
        if (dto != null) {
            jugadorActual = dto;
            regMensaje.setText(" ");
            actualizarBienvenida(jugadorActual.nombre, jugadorActual.puntaje);
            cargarCaballos();
            mostrar(PANTALLA_CABALLOS);
        } else {
            regMensaje.setText("Ese mail ya esta registrado.");
        }
    }

    // Pantalla: Seleccion de caballo
    private JPanel crearPantallaCaballos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        labelBienvenida = new JLabel(" ", SwingConstants.CENTER);
        labelBienvenida.setFont(new Font("SansSerif", Font.BOLD, 14));
        panel.add(labelBienvenida, BorderLayout.NORTH);

        modeloCaballos = new DefaultListModel<>();
        listaCaballos = new JList<>(modeloCaballos);
        listaCaballos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaCaballos.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JPanel centro = new JPanel(new BorderLayout(0, 6));
        centro.add(new JLabel("Elegi tu caballo:"), BorderLayout.NORTH);
        centro.add(new JScrollPane(listaCaballos), BorderLayout.CENTER);
        panel.add(centro, BorderLayout.CENTER);

        JPanel sur = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton btnIniciar = new JButton("Iniciar carrera");
        btnIniciar.addActionListener(e -> accionIniciarCarrera());
        JButton btnHistorial = new JButton("Ver historial");
        btnHistorial.addActionListener(e -> { cargarHistorial(); mostrar(PANTALLA_HISTORIAL); });
        sur.add(btnIniciar);
        sur.add(btnHistorial);
        panel.add(sur, BorderLayout.SOUTH);

        return panel;
    }

    private void cargarCaballos() {
        caballoSeleccionado = null;
        caballosDisponibles = controladorCaballo.getCaballosDisponibles();
        modeloCaballos.clear();
        for (CaballoDTO c : caballosDisponibles) {
            modeloCaballos.addElement(
                String.format("%-14s  tipo: %-9s  velocidad: %d",
                    c.nombre, tipoDesdeCadena(c.color), c.velocidadBase));
        }
        if (!modeloCaballos.isEmpty()) {
            listaCaballos.setSelectedIndex(0);
        }
    }

    private void accionIniciarCarrera() {
        int idx = listaCaballos.getSelectedIndex();
        if (idx < 0 || idx >= caballosDisponibles.size()) {
            JOptionPane.showMessageDialog(this, "Selecciona un caballo primero.");
            return;
        }
        caballoSeleccionado = caballosDisponibles.get(idx).nombre;
        controladorCarrera.prepararCarrera(DISTANCIA_CARRERA);

        prepararBarras(controladorCaballo.getCaballosDisponibles());
        labelTurno.setText("Turno: 0");
        mostrar(PANTALLA_CARRERA);
        iniciarTimer();
    }

    // Pantalla: Carrera (barras de progreso)
    private JPanel crearPantallaCarrera() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        labelTurno = new JLabel("Turno: 0", SwingConstants.CENTER);
        labelTurno.setFont(new Font("SansSerif", Font.BOLD, 14));
        panel.add(labelTurno, BorderLayout.NORTH);

        panelBarras = new JPanel();
        panelBarras.setLayout(new GridLayout(0, 1, 0, 10));
        panel.add(panelBarras, BorderLayout.CENTER);

        return panel;
    }

    //Barras de progreso
    private void prepararBarras(List<CaballoDTO> caballos) {
        panelBarras.removeAll();
        barrasPorCaballo.clear();
        etiquetasPorCaballo.clear();
        for (CaballoDTO c : caballos) {
            JProgressBar barra = new JProgressBar(0, DISTANCIA_CARRERA);
            barra.setValue(0);
            barrasPorCaballo.put(c.nombre, barra);

            String etiqueta = c.nombre;
            if (c.nombre.equals(caballoSeleccionado)) {
                etiqueta += " (TU CABALLO)";
            }
            JLabel lblNombre = new JLabel(etiqueta);
            lblNombre.setPreferredSize(new Dimension(150, 20));

            JLabel lblMetros = new JLabel("0 m", SwingConstants.CENTER);
            lblMetros.setVerticalAlignment(SwingConstants.BOTTOM);
            etiquetasPorCaballo.put(c.nombre, lblMetros);

            JPanel contenedorBarra = new JPanel(new BorderLayout(0, 0));
            contenedorBarra.add(lblMetros, BorderLayout.NORTH);
            contenedorBarra.add(barra, BorderLayout.CENTER);

            JPanel fila = new JPanel(new BorderLayout(8, 0));
            fila.add(lblNombre, BorderLayout.WEST);
            fila.add(contenedorBarra, BorderLayout.CENTER);
            panelBarras.add(fila);
        }
        panelBarras.revalidate();
        panelBarras.repaint();
    }

    private void iniciarTimer() {
        if (timerCarrera != null && timerCarrera.isRunning()) {
            timerCarrera.stop();
        }
        timerCarrera = new Timer(500, e -> {
            EstadoCarreraDTO estado = controladorCarrera.avanzarTurno();
            labelTurno.setText("Turno: " + estado.turno);
            for (CaballoDTO c : estado.caballos) {
                JProgressBar barra = barrasPorCaballo.get(c.nombre);
                if (barra != null) {
                    barra.setValue(Math.min(c.distRecorrida, DISTANCIA_CARRERA));
                    JLabel lbl = etiquetasPorCaballo.get(c.nombre);
                    if (lbl != null) lbl.setText(c.distRecorrida + " m");
                }
            }
            if (estado.hayGanador) {
                timerCarrera.stop();
                Timer pausa = new Timer(800, ev -> mostrarResultado());
                pausa.setRepeats(false);
                pausa.start();
            }
        });
        timerCarrera.start();
    }

    // Pantalla: Resultado
    private JPanel crearPantallaResultado() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel norte = new JPanel(new GridLayout(0, 1, 0, 4));

        JLabel titulo = new JLabel("Resultado de la carrera", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        norte.add(titulo);

        labelGanador = new JLabel(" ", SwingConstants.CENTER);
        labelGanador.setFont(new Font("SansSerif", Font.BOLD, 15));
        norte.add(labelGanador);

        labelPuntaje = new JLabel(" ", SwingConstants.CENTER);
        norte.add(labelPuntaje);

        labelPuntajeTotal = new JLabel(" ", SwingConstants.CENTER);
        norte.add(labelPuntajeTotal);

        panel.add(norte, BorderLayout.NORTH);

        areaResultado = new JTextArea(8, 30);
        areaResultado.setEditable(false);
        areaResultado.setFont(new Font("Monospaced", Font.PLAIN, 13));
        panel.add(new JScrollPane(areaResultado), BorderLayout.CENTER);

        JPanel sur = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnJugarDeNuevo = new JButton("Jugar de nuevo");
        btnJugarDeNuevo.addActionListener(e -> {
            actualizarBienvenida(jugadorActual.nombre, jugadorActual.puntaje);
            cargarCaballos();
            mostrar(PANTALLA_CABALLOS);
        });
        sur.add(btnJugarDeNuevo);
        panel.add(sur, BorderLayout.SOUTH);

        return panel;
    }

    private void mostrarResultado() {
        ResultadoDTO resultado = controladorCarrera.obtenerResultado(jugadorActual, caballoSeleccionado);
        jugadorActual.puntaje += resultado.puntajeObtenido;
        labelGanador.setText("Ganador: " + resultado.ganador);
        labelPuntaje.setText("Puntos obtenidos: +" + resultado.puntajeObtenido);
        labelPuntajeTotal.setText("Puntaje acumulado: " + jugadorActual.puntaje);

        StringBuilder sb = new StringBuilder("Posiciones finales:\n\n");
        for (int i = 0; i < resultado.posiciones.size(); i++) {
            sb.append(String.format("  %d. %s%n", i + 1, resultado.posiciones.get(i)));
        }
        areaResultado.setText(sb.toString());
        areaResultado.setCaretPosition(0);
        mostrar(PANTALLA_RESULTADO);
    }

    // Pantalla: Historial
    private JPanel crearPantallaHistorial() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("Historial de Carreras", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        panel.add(titulo, BorderLayout.NORTH);

        String[] columnas = {"Fecha", "Tu Caballo", "Ganador", "Posicion", "Puntos"};
        historialTableModel = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        historialTable = new JTable(historialTableModel);
        panel.add(new JScrollPane(historialTable), BorderLayout.CENTER);

        JPanel sur = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnVolver = new JButton("Volver");
        btnVolver.addActionListener(e -> mostrar(PANTALLA_CABALLOS));
        sur.add(btnVolver);
        panel.add(sur, BorderLayout.SOUTH);

        return panel;
    }

    private void cargarHistorial() {
        historialData = controladorCarrera.getHistorial(jugadorActual);
        historialTableModel.setRowCount(0);
        if (historialData.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aun no participaste en ninguna carrera.");
            return;
        }
        for (HistorialDTO h : historialData) {
            historialTableModel.addRow(new Object[]{
                h.fecha, h.caballoElegido, h.ganador, h.posicion, "+" + h.puntajeObtenido
            });
        }
    }


    // Utilidades
    private void actualizarBienvenida(String nombre, int puntaje) {
        labelBienvenida.setText("Bienvenido, " + nombre + "!   |   Puntaje: " + puntaje);
    }

    private String tipoDesdeCadena(String color) {
        if (color == null) return "";
        switch (color) {
            case "GREEN":  return "Veloz";
            case "RED":    return "Lento";
            case "YELLOW": return "Estandar";
            case "BLUE":   return "Random";
            default:       return "";
        }
    }
}
