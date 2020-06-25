package AnalizadorDOsCero;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FileDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.UIManager;

public class IDELast implements KeyListener, MouseWheelListener, MouseListener {

	private JFrame frame;
	/*
	 * Declaracion de variables y elementos que sirven como globales
	*/
	JTextArea areaTrabajo;
	JTextPane lineas;
	TextArea at;
	JTextField ct;
	FileDialog fd;
	JScrollPane sp,sp3;
	JLabel etru;
	String reservada; //inica la palabra reservada del metodo
	HighlightPainter colorin;
	ListaSencilla tokens = new ListaSencilla();
	static String salida, consola, consolaS, err; 
	int pos = 0, pos2 = 0, fin = 0, fin2 = 0;
	
	/*
	 * Método main, y creacion de un frame utilizando la clase creada en WinBuild
	*/
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IDELast window = new IDELast();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public IDELast() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.LIGHT_GRAY);
		frame.setTitle("PStudio");
		frame.setBounds(100, 100, 1128, 737);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(1,2));
		
		JMenuBar barraM = new JMenuBar();
		JMenu archivo = new JMenu("Archivo");
		JMenu opciones = new JMenu("Opciones");
		JMenu run = new JMenu("Run");
	
		barraM.setBackground(SystemColor.controlHighlight);
		
		/*
		 * Botón Abrir en el menu y su actionPerformed, este sirve para abrir documentos
		*/
		JMenuItem abrir = new JMenuItem("Abrir");
		archivo.add(abrir);
		abrir.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				JFileChooser seleccionado = new JFileChooser();
				File archiv;
				Buscador b = new Buscador();
				FileNameExtensionFilter fil= new FileNameExtensionFilter("PAU","pau");
				seleccionado.setFileFilter(fil);
				seleccionado.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

				if (seleccionado.showDialog(frame, "Abrir") == JFileChooser.APPROVE_OPTION) {
					archiv = seleccionado.getSelectedFile();
					if (archiv.canRead()) {
						if (archiv.getName().endsWith("pau")) {
							String contenido = b.AbrirTexto(archiv);
							areaTrabajo.setText(contenido);
							nlineaTextArea();
							ct.setText(""+seleccionado.getSelectedFile().getAbsolutePath());
							reservada = seleccionado.getSelectedFile().getName();
							String []palabraid=reservada.split("\\.");
						}
					}
				}
			}
		});
		
		/*
		 * Botón Grabar en el menu y su actionPerformed, este sirve para guardar documentos existentes
		*/
		JMenuItem grabar = new JMenuItem("Guardar");
		archivo.add(grabar);
		grabar.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				String ruta=ct.getText();
				if(ruta.compareTo("")!=0) {
					String contenido=areaTrabajo.getText();
					Grabar(ruta,contenido);
				}else {
					JOptionPane.showMessageDialog(null, "No hay una ruta especifica para el archivo");
				}
			}
		});
		
		
		/*
		 * Botón Guardar como... en el menu y su actionPerformed, este sirve para crear documentos nuevos
		*/
		JMenuItem gua = new JMenuItem("Guardar como..");
		archivo.add(gua);
		gua.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JFileChooser seleccionado = new JFileChooser();
				
				FileNameExtensionFilter fil= new FileNameExtensionFilter("PAU","pau");
				seleccionado.setFileFilter(fil);
				File archiv;
				Buscador b = new Buscador();
				if (seleccionado.showDialog(null, "Guardar como") == JFileChooser.APPROVE_OPTION) {
					archiv = seleccionado.getSelectedFile();
					if(archiv.getName().endsWith("pau")) {
						String conf=b.guardar(archiv, areaTrabajo.getText());
						if(conf!=null) {
							JOptionPane.showMessageDialog(null, conf);
							areaTrabajo.setText("");
						}else {
							JOptionPane.showMessageDialog(null, "Formato invalido");
						}
					}else {
						JOptionPane.showMessageDialog(null, "Formato no valido");
					}
				}
			}
		});
		
		/*
		 * Cambia el color del editor (Area de Texto)
		*/
		JMenuItem editor = new JMenuItem("Color de Editor");
		opciones.add(editor);
		editor.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				colorBack();
			}
		});
		
		/*
		 * Cambia el color de la fuente
		*/
		JMenuItem fuente = new JMenuItem("Color de Fuente");
		opciones.add(fuente);
		fuente.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				colorFont();
			}
		});
		
		/*
		 * Aquí mandamos a llamar a la clase de análisis lexico, con el botón run analisis
		*/
		JMenuItem ana = new JMenuItem("Compilar");
		ana.setIcon(new ImageIcon(IDELast.class.getResource("/AnalizadorDOsCero/img/play.png")));
		run.add(ana);		
		
		ana.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (!(ct.getText().length() < 1)) {

					tokens.clear();
					//Lexer lexer = new Lexer();
					Alexico lexer23= new Alexico(ct.getText());
					//lexer.LexerL(ct.getText(), areaTrabajo);
					String error = "";
					boolean bandera=false;
					String lineaE="";
					Sintak sintax = new Sintak();
					Highlighter lexH = areaTrabajo.getHighlighter();
					Highlighter sinH = areaTrabajo.getHighlighter();
					lexH.removeAllHighlights();
					sinH.removeAllHighlights();

					// Se borra lo que tiene la consola
					consola = "";
					consolaS = "";
					err = "";
					consola = consola + "Análisis Léxico\n";
					consola = consola + "-----------------\n";
					consolaS = consolaS + "Análisis Sintáctico\n";
					consolaS = consolaS + "-----------------\n";

					while (!lexer23.isExausthed()) { // Este es equivalente al HASNEXT
						if (lexer23.currentLexema() != null && lexer23.blanco==false) {
							System.out.println("Lexema-->"+lexer23.currentLexema());
							tokens.addValue(lexer23.currentToken() + ""); // Solo para comprobar
							if (tokens.getValor(tokens.listLenght() - 1).equalsIgnoreCase("error")) {
								try {
									System.out.println("linea de error"+(lexer23.nlinea-1));
									pos = areaTrabajo.getLineStartOffset(lexer23.nlinea-1);
									fin = areaTrabajo.getLineEndOffset(lexer23.nlinea-1);
								} catch (BadLocationException e1) {
									e1.printStackTrace();
								}
								try {
									if (pos >= 0)
										lexH.addHighlight(pos, fin, DefaultHighlighter.DefaultPainter);
								} catch (BadLocationException e1) {
									e1.printStackTrace();
								}
								bandera=true;
								lineaE+=lexer23.nlinea+", ";
								if (!error.contains(" en linea " + lexer23.nlinea + "\n"))
									error += ("Error léxico: " + lexer23.currentLexema() + " en linea " + lexer23.nlinea
											+ "\n");
							}else if(!(tokens.getValor(tokens.listLenght()-1).equalsIgnoreCase("comment") || tokens.getValor(tokens.listLenght()-1).equalsIgnoreCase("ini_com"))) {
								if(!bandera==true) {
									if (!sintax.AS(lexer23.currentToken() + "", lexer23.nlinea, lexer23.currentLexema())) {//bandera es para que truene si es lexico el error										System.out.println("token de mota: "+lexer23.currentToken());
											consola = consola + lexer23.genToken.tokenlexemanum() + "\n"; // Luego se imprime
											consolaS = consolaS + sintax.MensajeDePila;
											try {
												pos2 = areaTrabajo.getLineStartOffset(lexer23.nlinea-1);
												fin2 = areaTrabajo.getLineEndOffset(lexer23.nlinea-1);

											} catch (BadLocationException e1) {
												e1.printStackTrace();
											}
											try {
												if (pos2 >= 0)
													sinH.addHighlight(pos2, fin2,
															new DefaultHighlighter.DefaultHighlightPainter(
																	new Color(255, 150, 0)));
											} catch (BadLocationException e1) {
												e1.printStackTrace();
											}
										
									} else {
										consola = consola + lexer23.genToken.tokenlexemanum() + "\n"; // Luego se imprime
										consolaS = consolaS + sintax.MensajeDePila;
									}
								}else {
									consola = consola + lexer23.genToken.tokenlexemanum() + "\n"; // Luego se imprime
								}
							}else {
								consola = consola + lexer23.genToken.tokenlexemanum() + "\n"; // Luego se imprime
							}
						}
						lexer23.siguiente();// Avanza
					}
					if (lexer23.isSuccessful() && !tokens.contiene("error")) {
						consola = consola + "-----------------\n";
						consola = consola + "Análisis Léxico finalizado correctamente\n";
						consola = consola + "-----------------\n";
						err = err + "Análisis Léxico finalizado correctamente\n";
					} else { 
						consola = consola + "-----------------\n";
						consola = consola + "Análisis Léxico finalizado con errores\n";
						consola = consola + "-----------------\n";
						err = err + "Análisis Léxico finalizado con errores\n";
						consola = consola + error + "\n"; // Imprime los errores
						consola = consola + lexer23.mensajeError() + "\n";
						consola = consola + "-----------------\n";
					}lexer23.genToken.imprimeTokens();
					if (sintax.aceptado()) {
						consolaS = consolaS + "-----------------\n";
						consolaS = consolaS + "Análisis Sintáctico finalizado correctamente\n";
						consolaS = consolaS + "-----------------\n";
						err = err + "Análisis Sintáctico finalizado correctamente\n";
					} else {
						while (sintax.verfinales() > 0 && bandera!=true) { //checa si hay finales
							if (!(sintax.finales(" ", lexer23.nlinea))) {
								consolaS=consolaS + sintax.MensajeDePila;
								try {
									System.out.println("pos "+lexer23.nlinea);
									pos2 = areaTrabajo.getLineStartOffset(lexer23.nlinea-1);
									fin2 = areaTrabajo.getLineEndOffset(lexer23.nlinea-1);

								} catch (BadLocationException e1) {
									e1.printStackTrace();
								}
								try {
									if (pos2 >= 0)
										sinH.addHighlight(pos2, fin2,
												new DefaultHighlighter.DefaultHighlightPainter(
														new Color(255, 150, 0)));
								} catch (BadLocationException e1) {
									e1.printStackTrace();
								}
							}
						}sintax.cera=0; //reiniciamos
						//en este caso solo avisamos ya que la parte lexico sintactica se hacen juntas
						if (bandera==true) {
							consolaS = consolaS + "-----------------\n";
							consolaS = consolaS + "Análisis Sintáctico finalizado con errores\n";
							consolaS = consolaS + "-----------------\n";
							err = err + "Análisis Sintáctico finalizado con errores\n";
							consolaS = consolaS + "Se produjo un error lexico, en la linea "+lineaE+" parando la parte sintactica" + "\n"; // Imprime los errores
							consolaS = consolaS + "-----------------\n";
						}else {
						consolaS = consolaS + "-----------------\n";
						consolaS = consolaS + "Análisis Sintáctico finalizado con errores\n";
						consolaS = consolaS + "-----------------\n";
						err = err + "Análisis Sintáctico finalizado con errores\n";
						consolaS = consolaS + sintax.MensajeDeError + "\n"; // Imprime los errores
						consolaS = consolaS + "-----------------\n";
					}
				}
				int dialogResult = JOptionPane.YES_NO_OPTION;
				dialogResult = JOptionPane.showConfirmDialog(null,
						err+"\n"+"Ver consola?", "Finalizado", dialogResult);
				if (dialogResult == JOptionPane.YES_OPTION) {
					inicio();
				}
			} else {
				JOptionPane.showMessageDialog(null, "No hay archivos abiertos");
			}
		}
	});
		
		barraM.add(archivo);
		barraM.add(opciones);
		barraM.add(run);
		frame.setJMenuBar(barraM);
		
		areaTrabajo = new JTextArea();
		areaTrabajo.setForeground(Color.DARK_GRAY);
		areaTrabajo.setFont(new Font("Tahoma", Font.PLAIN, 19));
		areaTrabajo.addKeyListener(this);
		areaTrabajo.addMouseWheelListener(this);
		areaTrabajo.addMouseListener(this);

		sp = new JScrollPane(areaTrabajo);
		sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		sp.getVerticalScrollBar().setBackground(SystemColor.control);
		sp.setBackground(Color.white);
		sp.setOpaque(true);
		
		lineas = new JTextPane();
		lineas.setText("   ");
		lineas.setEditable(false);
		lineas.setFont(new Font("Tahoma", Font.PLAIN, 19));
		lineas.setBackground(SystemColor.controlHighlight);
		sp3 = new JScrollPane(lineas);
		sp3.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		sp3.getVerticalScrollBar().setBackground(SystemColor.control);
		sp3.setBackground(Color.white);
		sp3.setOpaque(true);
		
		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.control);
		etru = new JLabel("Ruta del archivo");
		ct = new JTextField(40);
		ct.setForeground(Color.GRAY);
		ct.setEditable(false);
		ct.setFont(new Font("Tahoma", Font.BOLD, 13));
		panel.add(etru);
		panel.add(ct);
		
		JPanel areas = new JPanel(new BorderLayout());
		areas.add(panel, BorderLayout.SOUTH);
		
		JButton openConsole = new JButton("Consola");
		openConsole.setBackground(Color.LIGHT_GRAY);
		panel.add(openConsole);
		areas.add(sp, BorderLayout.CENTER);
		areas.add(sp3, BorderLayout.WEST);
		
		frame.getContentPane().add(areas);
		sp.getViewport().addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				sp3.getVerticalScrollBar().setValue(sp.getVerticalScrollBar().getValue());
			}
		});
		
		openConsole.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				inicio();
			}
		});
	}

	/*
	 * La clase grabar llamada desde guardar
	*/
	public void Grabar(String r, String c) {
		 FileWriter F;
		 try {
			 F=new FileWriter(r);
			 F.write(c);
			 F.close();
		 }catch(IOException e) {
			 JOptionPane.showMessageDialog(null, "Error de escritura");
		 }
	 }
	
	/*
	 * La clase colorBack llamada desde color de fondo
	*/
	public void colorBack() {
        //presenta el dialogo de selección de colores
        Color color = JColorChooser.showDialog(null, "Selección de Color", Color.black);
        if (color != null) {    //si un color fue seleccionado
            //se establece como color del fuente y cursor
            areaTrabajo.setBackground(color);
        }
    }

	/*
	 * La clase colorFont llamada desde color de fuente
	*/
	public void colorFont() {
        //presenta el dialogo de selección de colores
        Color color = JColorChooser.showDialog(null, "Selección de Color", Color.black);
        if (color != null) {    //si un color fue seleccionado
            //se establece como color del fuente y cursor
            areaTrabajo.setForeground(color);
        }
    }
	
	
	/*
	 * Eventos key, para el número de lineas en el Area de Texto Líneas
	*/
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void keyPressed(KeyEvent e) {
		nlineaTextArea();
	}
	
	public void nlineaTextArea() {
		lineas.setText("");
		for (int i = 0; i < areaTrabajo.getLineCount(); i++) {
			lineas.setText(lineas.getText()+(i+1)+"\n");
		}
		sp3.getVerticalScrollBar().setValue(sp.getVerticalScrollBar().getValue());
	}
	public void keyReleased(KeyEvent e) {
		
	}
	
	/*
	 * Se da movimiento a ambos scroll con los mismos valores
	*/
	public void mouseWheelMoved(MouseWheelEvent e) {
	      //indica el valor del mouse
	       int notches = e.getWheelRotation();
	       //arriba
	       if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
	           sp.getVerticalScrollBar().getUnitIncrement(1);
	       } else { //scroll type == MouseWheelEvent.WHEEL_BLOCK_SCROLL lo bloquea
	    	   sp.getVerticalScrollBar().getBlockIncrement(1);
	       }
	}

	/*
	 * Esta parte dará movimiento a los scrolls haciendo uso del MouseScroll, para una mejor experiencia
	*/
	public void mouseClicked(MouseEvent arg0) {
		sp3.getVerticalScrollBar().setValue(sp.getVerticalScrollBar().getValue());
	}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent arg0) {
		sp3.getVerticalScrollBar().setValue(sp.getVerticalScrollBar().getValue());
	}
	public void mouseReleased(MouseEvent arg0) {
		sp3.getVerticalScrollBar().setValue(sp.getVerticalScrollBar().getValue());
	}
	
	public void inicio() {
		Consola ini = new Consola(frame, true, consola, consolaS);
		ini.setVisible(true);
	}
}