package gm.tienda_libros.vista;

import gm.tienda_libros.modelo.Libro;
import gm.tienda_libros.servicio.LibroServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Component
public class LibroForm extends JFrame {

    LibroServicio libroServicio;

    private JPanel panel;
    private JTable tablaLibros;
    private JTextField libroTexto;
    private JTextField autorTexto;
    private JTextField precioTexto;
    private JTextField existenciasTexto;
    private JButton agregarButton;
    private JButton modificarButton;
    private JButton eliminarButton;
    private DefaultTableModel tablaModeloLibros;

    private JTextField idTexto;

    final int idColumna = 0;
    final int libroColumna = 1;
    final int autorColumna = 2;
    final int precioColumna = 3;
    final int existenciasColumna = 4;

    @Autowired
    public LibroForm(LibroServicio libroServicio){
        this.libroServicio = libroServicio;
        iniciarForma();
        agregarButton.addActionListener(e -> agregarLibro());
        tablaLibros.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                cargarLibroSeleccionado();
            }
        });
        modificarButton.addActionListener(e -> modificarLibro());
        eliminarButton.addActionListener(e -> eliminarLibro());
    }

    private void eliminarLibro() {

        int renglon = tablaLibros.getSelectedRow();


        if(renglon!=-1) {
            //Recuperamos los valores de la tabla
            Integer idLibro = Integer.valueOf(idTexto.getText());
            String nombreLibro = libroTexto.getText();
            String autorLibro = autorTexto.getText();
            Double precioLibro = Double.valueOf(precioTexto.getText());
            Integer existenciasLibro = Integer.valueOf(existenciasTexto.getText());

            Libro libro = new Libro(idLibro, nombreLibro, autorLibro, precioLibro, existenciasLibro);

            int opcion =
                    JOptionPane.showConfirmDialog(this, "¿Esta seguro que desea eliminar el libro '" + nombreLibro + "'?",
                            "Confirmar eliminacion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (opcion == JOptionPane.YES_OPTION) {
                libroServicio.eliminarLibro(libro);
                mostrarMensaje("Libro eliminado con exito!");
                listarLibros();
                this.idTexto.setText("");
            }
        }else{
            mostrarMensaje("Debe seleccionar un registro..");
        }

    }

    private void modificarLibro() {

        if(this.idTexto.getText()!=""){
            if(libroTexto.getText()!=""){
                //Recuperamos los valores de la tabla
                Integer idLibro = Integer.valueOf(idTexto.getText());
                String nombreLibro = libroTexto.getText();
                String autorLibro = autorTexto.getText();
                Double precioLibro = Double.valueOf(precioTexto.getText());
                Integer existenciasLibro = Integer.valueOf(existenciasTexto.getText());

                Libro libro = new Libro(idLibro, nombreLibro, autorLibro, precioLibro, existenciasLibro);

                libroServicio.guardarLibro(libro);

                mostrarMensaje("Libro actualizado!");
                listarLibros();
                this.idTexto.setText("");
            }else{
                mostrarMensaje("Proporciona el nombre del libro..");
                this.libroTexto.requestFocusInWindow();
            }
        }else{
            mostrarMensaje("Debe seleccionar un libro..");
            this.tablaLibros.requestFocusInWindow();
        }

    }

    private void cargarLibroSeleccionado() {
        //Los indices de las columnas de las jtable inician en 0
        int renglon = tablaLibros.getSelectedRow();


        if(renglon!=-1){//Regresa -1 si no se selecciono ningun registro
            String idLibro = tablaLibros.getModel().getValueAt(renglon, idColumna).toString();
            idTexto.setText(idLibro);

            //Recuperamos los valores de la tabla
            String nombreLibro = tablaLibros.getModel().getValueAt(renglon, libroColumna).toString();
            String autorLibro = tablaLibros.getModel().getValueAt(renglon, autorColumna).toString();
            String precioLibro = tablaLibros.getModel().getValueAt(renglon, precioColumna).toString();
            String existenciasLibro = tablaLibros.getModel().getValueAt(renglon, existenciasColumna).toString();

            //Lo mostramos en el formulario
            libroTexto.setText(nombreLibro);
            autorTexto.setText(autorLibro);
            precioTexto.setText(precioLibro);
            existenciasTexto.setText(existenciasLibro);

        }else{
            this.tablaLibros.clearSelection();
        }
    }

    private void agregarLibro() {
        //Leer los valores del formulario
        if(libroTexto.getText().equals("")){
            mostrarMensaje("Proporciona el nombre del Libro");
            libroTexto.requestFocusInWindow();
            return;
        }

        String nombreLibro = libroTexto.getText();
        String autor  = autorTexto.getText();
        Double precio = Double.parseDouble(precioTexto.getText());
        Integer existencias = Integer.parseInt(existenciasTexto.getText());

        //Creamos el objeto de tipo libro
        Libro libro = new Libro();
        libro.setAutor(autor);
        libro.setNombreLibro(nombreLibro);
        libro.setPrecio(precio);
        libro.setExistencias(existencias);

        //Guardamos el libro
        this.libroServicio.guardarLibro(libro);

        mostrarMensaje("Se agrego el libro!");

        limpiarFormulario();
        listarLibros();
    }

    private void limpiarFormulario() {
        libroTexto.setText("");
        autorTexto.setText("");
        precioTexto.setText("");
        existenciasTexto.setText("");
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    private void iniciarForma(){
        setContentPane(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(900,700);
//        Toolkit toolkit = Toolkit.getDefaultToolkit();
//        Dimension sizePantalla = toolkit.getScreenSize();
//        int x = (sizePantalla.width - getWidth()/2);
//        int y = (sizePantalla.height - getHeight()/2);
//        setLocation(x,y);
        setLocationRelativeTo(null);
    }

    //Personalizamos la creacion de la tabla
    private void createUIComponents() {

        //Creamos el elemento idTexto oculto
        idTexto = new JTextField("");
        idTexto.setVisible(false);

        this.tablaModeloLibros = new DefaultTableModel(0,5) {
            //Quitamos la funcionalidad de editar directamente
            @Override
            public boolean isCellEditable(int row, int column){return false;}
        };

        //Colocamos los headers
        String[] cabeceros  = {"Id","Libro", "Autor", "Precio", "Existencias"};
        tablaModeloLibros.setColumnIdentifiers(cabeceros);

        //Instanciar el objeto JTable
        this.tablaLibros = new JTable(tablaModeloLibros);

        tablaLibros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        listarLibros();
    }

    private void listarLibros(){
        // Limpiar la tabla
        tablaModeloLibros.setRowCount(0);;
        // Obtener los libros
        var libros = libroServicio.listarLibros();

        libros.forEach((libro)->{
            Object[] renglonLibro = {
                libro.getIdLibro(),
                libro.getNombreLibro(),
                libro.getAutor(),
                libro.getPrecio(),
                libro.getExistencias()
            };
            this.tablaModeloLibros.addRow(renglonLibro);
        });
    }
}
