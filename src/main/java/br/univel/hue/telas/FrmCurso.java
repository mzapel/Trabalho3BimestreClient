package br.univel.hue.telas;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;

import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.GridBagConstraints;

import javax.swing.JTextField;

import java.awt.Insets;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;

import org.apache.http.client.ClientProtocolException;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.w3c.dom.views.AbstractView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.univel.hue.entidades.Curso;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FrmCurso extends JFrame {

	private JPanel contentPane;
	private JTextField txtCurso;
	private JScrollPane scrollPane;
	private JTable table;
	private JButton btnSalvar;

	private List<Curso> cursos = new ArrayList<Curso>();
	private TableModelCurso tmc = new TableModelCurso();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FrmCurso frame = new FrmCurso();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FrmCurso() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 454);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 1.0, 0.0,
				Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 1.0,
				Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);
		
		JLabel lblCurso = new JLabel("Curso");
		GridBagConstraints gbc_lblCurso = new GridBagConstraints();
		gbc_lblCurso.insets = new Insets(0, 0, 5, 5);
		gbc_lblCurso.anchor = GridBagConstraints.EAST;
		gbc_lblCurso.gridx = 0;
		gbc_lblCurso.gridy = 0;
		contentPane.add(lblCurso, gbc_lblCurso);

		txtCurso = new JTextField();
		GridBagConstraints gbc_txtCurso = new GridBagConstraints();
		gbc_txtCurso.insets = new Insets(0, 0, 5, 5);
		gbc_txtCurso.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtCurso.gridx = 1;
		gbc_txtCurso.gridy = 0;
		contentPane.add(txtCurso, gbc_txtCurso);
		txtCurso.setColumns(10);

		btnSalvar = new JButton("Salvar");
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				try {
					Curso c = new Curso();
					c.setNome(txtCurso.getText());
					String json;

					ClientRequest req = new ClientRequest(
							"http://trabalho3bimestre-mzapel.rhcloud.com/Trabalho3Bimestre/rest/cursos");
					// "http://localhost:8080/trabalho3bimestre/rest/cursos");
					req.accept("application/json");

					ObjectMapper mapper = new ObjectMapper();

					json = mapper.writeValueAsString(c);
					req.body("application/json", json);

					ClientResponse<String> response = req.post(String.class);

					if (response.getStatus() != 201) {
						throw new RuntimeException(
								"Failed : HTTP error code : "
										+ response.getStatus());
					}

					listar();
					
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		GridBagConstraints gbc_btnSalvar = new GridBagConstraints();
		gbc_btnSalvar.insets = new Insets(0, 0, 5, 0);
		gbc_btnSalvar.gridx = 2;
		gbc_btnSalvar.gridy = 0;
		contentPane.add(btnSalvar, gbc_btnSalvar);

		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 3;
		gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 2;
		contentPane.add(scrollPane, gbc_scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);
		table.setModel(tmc);
		
		listar();
	}

	private void listar() {

		try {
			cursos.clear();
			ClientRequest request = new ClientRequest(
					"http://trabalho3bimestre-mzapel.rhcloud.com/Trabalho3Bimestre/rest/cursos");
			request.accept("application/json");
			ClientResponse<String> response = request.get(String.class);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					new ByteArrayInputStream(response.getEntity().getBytes())));

			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {

				ObjectMapper mapper = new ObjectMapper();
				Curso[] cursoarray = mapper.readValue(output, Curso[].class);

				for (Curso c : cursoarray) {
					cursos.add(c);
				}
			}
			
			tmc.fireTableDataChanged();

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

class TableModelCurso extends AbstractTableModel {

	public int getColumnCount() {
		return 2;
	}

	public int getRowCount() {
		return cursos.size();
	}

	public Object getValueAt(int row, int column) {
		switch (column) {
		case 0:
			return cursos.get(row).getId();
		case 1:
			return cursos.get(row).getNome();
		default:
			return "DEU PAU";
		}
	}
	
	@Override
	public String getColumnName(int column) {
		switch (column) {
		case 0:
			return "ID";
		case 1:
			return "Nome Curso";
		default:
			return "DEU PAU";
		}
	}
	
	
}
	
}
