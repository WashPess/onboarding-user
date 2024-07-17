// server.js
const express = require('express');
const bodyParser = require('body-parser');

const app = express();
const port = 3000;

// Middleware para analisar o corpo das requisições em JSON
app.use(bodyParser.json());

// Endpoint para registrar um novo usuário
app.post('/api/users/register', (req, res) => {
    // Aqui você acessaria os dados enviados pelo usuário
    const { document, firstName, lastName, nickName, password, confirmPassword } = req.body;

    // Implemente a lógica para registrar o usuário usando sua UserModel
    // Por exemplo: UserModel.create({ name, email, password })...

    UserModel.create({ document, firstName, lastName, nickName, password, confirmPassword });
    
    // Envie uma resposta indicando sucesso
    res.status(201).json({ message: 'User registered successfully' });
});

// Iniciar o servidor
app.listen(port, () => {
    console.log(`Server running on port ${port}`);
});