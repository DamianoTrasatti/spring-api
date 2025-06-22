const express = require('express');

const app = express();
app.use(express.json());
app.listen(3000);

/*          TRAMITE GET E SCRIVE CIAO
app.get('/', (req, res) => {
    res.send("ciao");
});
*/

/*          TRAMITE POST E SIMULA LA CORRETTA CREAZIONE DI UN POST
app.post('/creazione_post', (req, res) => {
    res.send("post creato!");
});
*/

const utenti = [
    {
        nome: 'Damiano',
        cognome: 'Trasatti'
    },
    {
        nome: 'Tommaso',
        cognome: 'Trasatti'
    },
    {
        nome: 'Filippo',
        cognome: 'Trasatti'
    }
]

const posts = [];

/*          PASSAGGIO DA PARAMETRO 
app.get('/users/:nome', (req, res) => { // nome è un parametro
    // res.send(utenti);
    const { nome } = req.params;

    const user = utenti.find(x => x.nome === nome);

    if (!user) res.sendStatus(404);
    else res.send(user)
});
*/

/*          SE LO PRENDO DA UNA QUERY
app.get('/users', (req, res) => { // non ho più parametri ma query
    const { nome } = req.query;

    const user = utenti.find(x => x.nome === nome);

    if (!user) res.sendStatus(404);
    else res.send(user)
});
*/

/* ORA LO PRENDIAMO DALL'HEADER
app.get('/users', (req, res) => { // non ho più parametri ma query
    const { nome } = req.headers;

    const user = utenti.find(x => x.nome === nome);

    if (!user) res.sendStatus(404);
    else res.send(user)
});
*/

app.post('/crea_post', (req, res) => {
    const { nomeUtente, titolo, contenuto } = req.body;

    // Utente esiste? 
    const user = utenti.find(x => x.nome === nomeUtente);

    if (!user) res.sendStatus(404);
    
    const nuovoPost = {
        id: posts.length + 1,
        autore: nomeUtente, titolo, contenuto,
        data: new Date().toISOString()
    };

    posts.push(nuovoPost);
    res.status(201).json(nuovoPost);
});

app.get('/posts', (req, res) => {
    res.json(posts);
})