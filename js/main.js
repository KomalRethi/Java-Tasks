// ===== ShopEase Cart Script (Final Stable Version with Quantity & Hidden Badge) =====
document.addEventListener("DOMContentLoaded", () => {
  // Load or initialize cart
  let cart = JSON.parse(localStorage.getItem("cart")) || [];

  // Save cart to localStorage
  const saveCart = () => localStorage.setItem("cart", JSON.stringify(cart));

  // ===== Update cart badge count =====
  function updateCartCount() {
    const countEl = document.getElementById("cart-count");
    if (countEl) {
      const totalQty = cart.reduce((sum, item) => sum + (item.quantity || 0), 0);
      if (totalQty > 0) {
        countEl.textContent = totalQty;
        countEl.style.display = "inline-block"; // show badge
      } else {
        countEl.style.display = "none"; // hide badge if 0
      }
    }
  }

  // ===== Add product to cart =====
  function addToCart(item) {
    const existing = cart.find(p => p.id === item.id);
    if (existing) {
      existing.quantity = (existing.quantity || 0) + 1;
      alert(`${existing.name} quantity increased to ${existing.quantity}.`);
    } else {
      item.quantity = 1;
      item.price = parseFloat(item.price);
      cart.push(item);
      alert(`${item.name} added to cart!`);
    }
    saveCart();
    updateCartCount();
  }

  // ===== Remove product completely =====
  function removeFromCart(id) {
    cart = cart.filter(p => p.id !== id);
    saveCart();
    renderCart();
    updateCartCount();
  }

  // ===== Change quantity by delta =====
  function changeQuantity(id, delta) {
    const item = cart.find(p => p.id === id);
    if (item) {
      item.quantity = (item.quantity || 0) + delta;
      if (item.quantity <= 0) {
        removeFromCart(id);
      } else {
        saveCart();
        renderCart();
        updateCartCount();
      }
    }
  }

  // ===== Render cart table in cart.html =====
  function renderCart() {
    const body = document.getElementById("cart-table-body");
    const totalEl = document.getElementById("cart-total");
    if (!body) return;

    cart = JSON.parse(localStorage.getItem("cart")) || [];

    body.innerHTML = "";
    let total = 0;

    if (cart.length === 0) {
      body.innerHTML = `<tr><td colspan="6" class="text-center">Your cart is empty!</td></tr>`;
      if (totalEl) totalEl.textContent = "$0.00";
      updateCartCount(); // ensures badge hides on empty
      return;
    }

    cart.forEach(item => {
      const price = parseFloat(item.price) || 0;
      const qty = parseInt(item.quantity) || 0;
      const subtotal = price * qty;
      total += subtotal;

      body.insertAdjacentHTML("beforeend", `
        <tr>
          <td><img src="${item.image}" width="60" class="rounded"></td>
          <td>${item.name}</td>
          <td>$${price.toFixed(2)}</td>
          <td>
            <div class="d-flex justify-content-center align-items-center">
              <button class="btn btn-sm btn-secondary decrease" data-id="${item.id}">−</button>
              <span class="mx-2">${qty}</span>
              <button class="btn btn-sm btn-secondary increase" data-id="${item.id}">+</button>
            </div>
          </td>
          <td>$${subtotal.toFixed(2)}</td>
          <td><button class="btn btn-danger btn-sm remove-item" data-id="${item.id}">Remove</button></td>
        </tr>
      `);
    });

    if (totalEl) totalEl.textContent = `$${total.toFixed(2)}`;

    // Add event listeners for remove and +/- buttons
    document.querySelectorAll(".remove-item").forEach(btn => {
      btn.addEventListener("click", () => removeFromCart(btn.getAttribute("data-id")));
    });
    document.querySelectorAll(".increase").forEach(btn => {
      btn.addEventListener("click", () => changeQuantity(btn.getAttribute("data-id"), 1));
    });
    document.querySelectorAll(".decrease").forEach(btn => {
      btn.addEventListener("click", () => changeQuantity(btn.getAttribute("data-id"), -1));
    });
  }

  // ===== Attach Add to Cart Buttons (on any page) =====
  document.querySelectorAll(".add-to-cart").forEach(btn => {
    btn.addEventListener("click", () => {
      const item = {
        id: btn.getAttribute("data-id"),
        name: btn.getAttribute("data-name"),
        price: parseFloat(btn.getAttribute("data-price")),
        image: btn.getAttribute("data-image"),
      };
      addToCart(item);
    });
  });

  // ===== Initialize Page =====
  updateCartCount();
  renderCart();
});
