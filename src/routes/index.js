const express = require('express');
const userRoutes = require('./userRoutes');

const router = express.Router();

// Import user routes

// Add user routes to the main router
router.use('/users', userRoutes);

module.exports = router;