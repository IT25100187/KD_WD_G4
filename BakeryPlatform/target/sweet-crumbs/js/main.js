/* Sweet Crumbs Bakery Platform — main.js */

document.addEventListener('DOMContentLoaded', function () {

  // ---- Live date in topbar ----
  var el = document.getElementById('topbarDate');
  if (el) {
    var d = new Date();
    el.textContent = d.toLocaleDateString('en-LK', {
      weekday: 'short', year: 'numeric', month: 'short', day: 'numeric'
    });
  }

  // ---- Auto-dismiss flash messages after 5 seconds ----
  document.querySelectorAll('.flash').forEach(function (flash) {
    setTimeout(function () {
      flash.style.transition = 'opacity 0.4s ease';
      flash.style.opacity = '0';
      setTimeout(function () { flash.remove(); }, 400);
    }, 5000);
  });

  // ---- Table row staggered fade-in ----
  document.querySelectorAll('.tr-hover').forEach(function (row, i) {
    row.style.opacity = '0';
    row.style.transform = 'translateY(8px)';
    setTimeout(function () {
      row.style.transition = 'opacity 0.2s ease, transform 0.2s ease';
      row.style.opacity = '1';
      row.style.transform = 'none';
    }, i * 25);
  });

  // ---- Stat card fade-in ----
  document.querySelectorAll('.stat-card').forEach(function (card, i) {
    card.style.opacity = '0';
    card.style.transform = 'translateY(12px)';
    setTimeout(function () {
      card.style.transition = 'opacity 0.25s ease, transform 0.25s ease, box-shadow 0.15s, transform 0.15s';
      card.style.opacity = '1';
      card.style.transform = 'none';
    }, i * 60);
  });

  // ---- Highlight today's pickup dates ----
  var today = new Date().toISOString().split('T')[0];
  document.querySelectorAll('.td-date').forEach(function (el) {
    if (el.textContent.trim() === today) {
      el.style.color = '#C8861C';
      el.style.fontWeight = '600';
      el.title = 'Pickup is TODAY!';
    }
  });

  // ---- Sidebar mobile toggle ----
  var toggle = document.querySelector('.sidebar-toggle');
  if (toggle) {
    toggle.addEventListener('click', function () {
      document.body.classList.toggle('sidebar-open');
    });
  }

  // ---- Customer name auto-fill from customer list (order form) ----
  var custIdInput = document.getElementById('customerId');
  if (custIdInput) {
    custIdInput.addEventListener('blur', function () {
      // If customerName is empty, nudge user
      var nameInput = document.getElementById('customerName');
      if (nameInput && !nameInput.value.trim()) {
        nameInput.style.borderColor = '#C8861C';
        nameInput.placeholder = 'Enter customer name for ID ' + custIdInput.value;
      }
    });
  }

  // ---- Confirm on all delete links (extra safety) ----
  document.querySelectorAll('a[href*="action=delete"]').forEach(function (link) {
    if (!link.getAttribute('onclick')) {
      link.addEventListener('click', function (e) {
        if (!confirm('Are you sure you want to delete this record?')) {
          e.preventDefault();
        }
      });
    }
  });
});
