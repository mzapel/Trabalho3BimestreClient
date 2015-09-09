package br.univel.hue.telas;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.EventQueue;

import javax.naming.LimitExceededException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;

import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.GridBagConstraints;

import javax.swing.JTextField;

import java.awt.Insets;

import javax.swing.JButton;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.apache.http.client.ClientProtocolException;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.univel.hue.entidades.Curso;
import br.univel.hue.entidades.Disciplina;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class FrmDisciplina extends JFrame {

	private JPanel contentPane;
	private JTextField txtNomeDisciplina;
	private JLabel lblNomeCurso;
	private JTextField txtNomeCurso;
	private JButton btnSalvar;
	private JScrollPane scrollPane;
	private JTable table;

	private List<Disciplina> disciplinas = new ArrayList<Disciplina>();
	private TableModelDisciplina tmd = new TableModelDisciplina();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FrmDisciplina frame = new FrmDisciplina();
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
	public FrmDisciplina() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 729, 440);
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

		JLabel lblNomeDisciplina = new JLabel("Nome Disciplina");
		GridBagConstraints gbc_lblNomeDisciplina = new GridBagConstraints();
		gbc_lblNomeDisciplina.insets = new Insets(0, 0, 5, 5);
		gbc_lblNomeDisciplina.anchor = GridBagConstraints.EAST;
		gbc_lblNomeDisciplina.gridx = 0;
		gbc_lblNomeDisciplina.gridy = 0;
		contentPane.add(lblNomeDisciplina, gbc_lblNomeDisciplina);

		txtNomeDisciplina = new JTextField();
		txtNomeDisciplina.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				txtNomeCurso.grabFocus();
			}
		});
		GridBagConstraints gbc_txtNomeDisciplina = new GridBagConstraints();
		gbc_txtNomeDisciplina.insets = new Insets(0, 0, 5, 5);
		gbc_txtNomeDisciplina.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtNomeDisciplina.gridx = 1;
		gbc_txtNomeDisciplina.gridy = 0;
		contentPane.add(txtNomeDisciplina, gbc_txtNomeDisciplina);
		txtNomeDisciplina.setColumns(10);

		lblNomeCurso = new JLabel("Nome Curso");
		GridBagConstraints gbc_lblNomeCurso = new GridBagConstraints();
		gbc_lblNomeCurso.anchor = GridBagConstraints.EAST;
		gbc_lblNomeCurso.insets = new Insets(0, 0, 5, 5);
		gbc_lblNomeCurso.gridx = 0;
		gbc_lblNomeCurso.gridy = 1;
		contentPane.add(lblNomeCurso, gbc_lblNomeCurso);

		txtNomeCurso = new JTextField();
		txtNomeCurso.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				btnSalvar.grabFocus();
			}
		});
		GridBagConstraints gbc_txtNomeCurso = new GridBagConstraints();
		gbc_txtNomeCurso.insets = new Insets(0, 0, 5, 5);
		gbc_txtNomeCurso.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtNomeCurso.gridx = 1;
		gbc_txtNomeCurso.gridy = 1;
		contentPane.add(txtNomeCurso, gbc_txtNomeCurso);
		txtNomeCurso.setColumns(10);

		btnSalvar = new JButton("Salvar");
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Disciplina d = new Disciplina();
					d.setNome(txtNomeDisciplina.getText());
					d.setCurso(txtNomeCurso.getText());
					String json;

					ClientRequest req = new ClientRequest(
					 "http://trabalho3bimestre-mzapel.rhcloud.com/Trabalho3Bimestre/rest/disciplinas");
					//		"http://localhost:8080/trabalho3bimestre/rest/disciplinas");
					req.accept("application/json");

					ObjectMapper mapper = new ObjectMapper();

					json = mapper.writeValueAsString(d);
					req.body("application/json", json);

					ClientResponse<String> response = req.post(String.class);

					if (response.getStatus() != 201 ) {
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
		gbc_btnSalvar.fill = GridBagConstraints.BOTH;
		gbc_btnSalvar.gridheight = 2;
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
		table.setModel(tmd);

		listar();

	}

	private void listar() {
		try {
			disciplinas.clear();
			ClientRequest request = new ClientRequest(
			 "http://trabalho3bimestre-mzapel.rhcloud.com/Trabalho3Bimestre/rest/disciplinas");
			//		"http://localhost:8080/trabalho3bimestre/rest/disciplinas");
			request.accept("application/json");
			ClientResponse<String> response = request.get(String.class);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					new ByteArrayInputStream(response.getEntity().getBytes())));

			String output;

			while ((output = br.readLine()) != null) {

				ObjectMapper mapper = new ObjectMapper();
				Disciplina[] discarray = mapper.readValue(output,
						Disciplina[].class);

				for (Disciplina d : discarray) {
					disciplinas.add(d);
				}
			}

			tmd.fireTableDataChanged();

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class TableModelDisciplina extends AbstractTableModel {

		public int getColumnCount() {
			return 3;
		}

		public int getRowCount() {
			return disciplinas.size();
		}

		public Object getValueAt(int row, int column) {
			switch (column) {
			case 0:
				return disciplinas.get(row).getId();
			case 1:
				return disciplinas.get(row).getNome();
			case 2:
				return disciplinas.get(row).getCurso();
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
				return "Disciplina";
			case 2:
				return "Curso";
			default:
				return "DEU PAU";
			}
		}

	}

}
