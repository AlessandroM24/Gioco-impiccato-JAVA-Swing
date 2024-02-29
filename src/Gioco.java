// MACALUSO ALESSANDRO 4^C INF. 12/02/2024

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Gioco extends JFrame {
    final private JTextField inputUtente;
    final private JButton bottone;
    final private JPanel pannello;
    final private JPanel lettere;
    private JLabel immagine;
    final private String PAROLA_SEGRETA = "Castello".toUpperCase(); // Parola da indovinare.
    final private ArrayList<Character> caratteriParolaSegreta = splitString(PAROLA_SEGRETA);
    final private ArrayList<Character> caratteriNascosti = nascontiLettere(caratteriParolaSegreta);
    private int fase = 0; // da 0 a 10.


    //COSTRUTTORE
    public Gioco() {
        this.inputUtente = new JTextField();
        this.bottone = new JButton("INDOVINA");
        this.pannello = new JPanel();
        this.lettere = new JPanel();
        this.immagine = new JLabel(new ImageIcon("img/fase0.jpg"));
        creaGUI();
    }

    private void creaGUI() {
        this.setIconImage(new ImageIcon("img/fase10.jpg").getImage()); // Immagine della finestra.
        this.setTitle("Gioco dell'impiccato");
        this.setSize(850, 650);
        this.setLocationRelativeTo(null); // La finestra appare al centro dello schermo.
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Corretta chiusura del programma.

        inputUtente.addActionListener(new AzioniBottone()); // Aggiunta dell'ascoltato quando si preme il tasto INVIO.

        inputUtente.setFont(new Font("Arial", Font.PLAIN, 40)); // Font dell'input dell'utente.
        bottone.setFont(new Font("Arial", Font.PLAIN, 35)); // Font del testo del bottone.
        bottone.addActionListener(new AzioniBottone()); // Aggiunta dell'interfaccia per eseguire azioni dopo il click.

        pannello.setLayout(new GridLayout(1, 2));
        lettere.add(new JLabel(caratteriNascosti.toString()));

        pannello.add(lettere);
        pannello.add(immagine);

        this.add(inputUtente, BorderLayout.NORTH);
        this.add(bottone, BorderLayout.SOUTH);
        this.add(pannello, BorderLayout.CENTER);
        this.setVisible(true);
    }


    /**
     * Questo metodo per eseguire lo split in un ArrayList tutte le lettere di una stringa.
     *
     * @param s stringa su cui eseguire lo split.
     * @return ArrayList carattere per carattere della stringa.
     */
    private ArrayList<Character> splitString(String s) {
        ArrayList<Character> caratteri = new ArrayList<>();

        for (int i = 0; i < s.length(); i++) {
            caratteri.add(s.charAt(i));
        }
        return caratteri;
    }


    /**
     * Convertire un ArrayList di caratteri in una stringa.
     *
     * @param caratteri ArrayList da cui prendere i caratteri.
     * @return stringa contenente i caratteri in ordine presenti nell'ArrayList.
     */
    private String ListToString(ArrayList<Character> caratteri) {
        StringBuilder stringa = new StringBuilder();

        for (Character carattere : caratteri) {
            stringa.append(carattere);
        }
        return stringa.toString();
    }


    /**
     * Questo metodo serve per ricevere un ArrayList di caratteri (ovvero la parola divisa carattere per carattere)
     * e impostare tutti i caratteri (tranne il primo e l'ultimo) con '_'. (sono le lettere nascoste).
     *
     * @param caratteri ArrayList in cui bisogna nascondere le lettere.
     * @return ArrayList con le lettere nascoste (tranne prima e ultima).
     */
    private ArrayList<Character> nascontiLettere(ArrayList<Character> caratteri) {
        ArrayList<Character> caratteriNascosti = new ArrayList<>();

        for (int i = 0; i < caratteri.size(); i++) {
            if (i != 0 && i != caratteri.size() - 1) {
                caratteriNascosti.add('_');
            } else {
                caratteriNascosti.add(caratteri.get(i));
            }
        }
        return caratteriNascosti;
    }


    public class AzioniBottone implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String parola = inputUtente.getText().toUpperCase();
            char carattere = parola.charAt(0);

            // Controlla se la parola inserita dall'utente è uguale a quella da indovinare.
            if ((parola.equalsIgnoreCase(PAROLA_SEGRETA))) {
                lettere.removeAll(); // Rimuove l'ArrayList precedente.
                lettere.revalidate(); // Aggiorna lettere (rettangolo sinistro)
                lettere.repaint();  // Aggiorna lettere (rettangolo sinistro).
                lettere.add(new JLabel(PAROLA_SEGRETA)); // Carica la parola segreta.
                pannello.add(lettere, 0); // Aggiunge la parola segreta al pannello (sinistra, indice 0).
                JOptionPane.showMessageDialog(null, "Hai indovinato con " + fase + " errore/i");
                System.exit(0); // Chide il programma con status 0.

            }
            // Controlla se l'input dell'utente è composta da un solo carattere e che il carattere sia presente nella
            // parola segreta.
            // In caso affermativo --> Prende tutti gli indici in cui è presente quella lettera e cambia l'ArrayList
            // "caratteriNascosti" impostando il carattere passato dall'utente al posto di '_'.
            // Ad esempio: PAROLA_SEGRETA = "castello". L'utente inserisce il carattere 'l' e il programma
            // cambia i '_' agli indici 5 e 6 impostandoli ad 'l' (carattere).
            else if (parola.length() == 1 && caratteriParolaSegreta.contains(carattere)) {
                ArrayList<Integer> indici = new ArrayList<>();

                // Ottenere tutti gli indici in cui quel carattere è presente.
                for (int i = 0; i < PAROLA_SEGRETA.length(); i++) {
                    if (carattere == PAROLA_SEGRETA.charAt(i)) {
                        indici.add(i);
                    }
                }

                // Cambia i caratteri '_' in carattere input utente.
                for (Integer indice : indici) {
                    caratteriNascosti.set(indice, carattere);
                }

                lettere.removeAll(); // Rimuove il precedente ArrayList (caratteriNascosti).
                lettere.revalidate(); // Aggiorna la rimozione.
                lettere.repaint(); // Aggiorna la rimozione.
                lettere.add(new JLabel(caratteriNascosti.toString())); // Aggiunge il JLabel con il nuovo ArrayList al
                // JPanel (lettere)
                pannello.add(lettere, 0); // Aggiunge lettere al pannello (sinistra, indice 0).

            } else {
                fase++;
                pannello.remove(immagine);
                // Revalidate e repaint servono per aggiornare l'immagine dopo averla rimossa.
                pannello.revalidate();
                pannello.repaint();

                String percorso = "img/fase" + fase + ".jpg"; // Percorso della foto (faseN.jpg).
                immagine = new JLabel(new ImageIcon(percorso)); // Imposta la nuova immagine.
                pannello.add(immagine);
            }
            inputUtente.setText(""); // Cancella l'input dell'utente.

            // Verifica se la parola inserita lettera per lettera (ArrayList presente a sinistra dello schermo)
            // è uguale a PAROLA_SEGRETA. Se è uguale appare un messaggio di vittoria e il programma termina.
            if (ListToString(caratteriNascosti).equalsIgnoreCase(PAROLA_SEGRETA)) {
                JOptionPane.showMessageDialog(null, "Hai indovinato con " + fase + " errore/i");
                System.exit(0);
            }

            // Se è arrivato alla fase10 (l'ultima) prima di aver indovinato, il programma avvisa della sconfitta e
            // Si chiude.
            if (fase == 10) {
                JOptionPane.showMessageDialog(null, "Hai perso, la parola era: " + PAROLA_SEGRETA);
                System.exit(0);
            }
        }
    }
}
