document.addEventListener('DOMContentLoaded', function() {
  // 1. Dark Mode Toggle
  initTheme();
  
  // 2. Sidebar Collapse Toggle
  initSidebar();
  
  // 3. Auto-dismiss alerts
  initAlerts();
});

// Theme Management
function initTheme() {
  const currentTheme = localStorage.getItem('theme') || 'light';
  if (currentTheme === 'dark') {
    document.body.classList.add('dark-theme');
    updateThemeToggleIcons(true);
  } else {
    document.body.classList.remove('dark-theme');
    updateThemeToggleIcons(false);
  }
}

function toggleTheme() {
  const isDark = document.body.classList.toggle('dark-theme');
  localStorage.setItem('theme', isDark ? 'dark' : 'light');
  updateThemeToggleIcons(isDark);
}

function updateThemeToggleIcons(isDark) {
  const icons = document.querySelectorAll('.theme-toggle-btn i');
  icons.forEach(icon => {
    if (isDark) {
      icon.className = 'fas fa-sun';
    } else {
      icon.className = 'fas fa-moon';
    }
  });
}

// Sidebar Management
function initSidebar() {
  const sidebar = document.querySelector('.admin-sidebar');
  const main = document.querySelector('.admin-main');
  const toggleBtn = document.querySelector('.sidebar-toggle-btn');
  
  if (sidebar && main && toggleBtn) {
    // Load state
    const collapsed = localStorage.getItem('sidebar-collapsed') === 'true';
    if (collapsed) {
      sidebar.classList.add('collapsed');
      main.classList.add('expanded');
    }
    
    toggleBtn.addEventListener('click', function() {
      const isCollapsed = sidebar.classList.toggle('collapsed');
      main.classList.toggle('expanded');
      localStorage.setItem('sidebar-collapsed', isCollapsed);
    });
  }
}

// Alert Autohide
function initAlerts() {
  const alerts = document.querySelectorAll('.alert');
  alerts.forEach(function(alert) {
    // Check if bootstrap is loaded
    setTimeout(function() {
      if (typeof bootstrap !== 'undefined') {
        const bsAlert = new bootstrap.Alert(alert);
        bsAlert.close();
      } else {
        alert.style.display = 'none';
      }
    }, 5000);
  });
}

// Toast Notifications System
function showToast(message, type = 'success') {
  let container = document.getElementById('toast-container');
  if (!container) {
    container = document.createElement('div');
    container.id = 'toast-container';
    container.className = 'position-fixed bottom-0 end-0 p-3';
    container.style.zIndex = '1100';
    document.body.appendChild(container);
  }
  
  const toastId = 'toast-' + Date.now();
  const bgClass = type === 'success' ? 'bg-success' : type === 'danger' ? 'bg-danger' : 'bg-info';
  const iconClass = type === 'success' ? 'fa-check-circle' : type === 'danger' ? 'fa-exclamation-circle' : 'fa-info-circle';
  
  const toastHTML = `
    <div id="${toastId}" class="toast align-items-center text-white ${bgClass} border-0 shadow" role="alert" aria-live="assertive" aria-atomic="true">
      <div class="d-flex">
        <div class="toast-body">
          <i class="fas ${iconClass} me-2"></i> ${message}
        </div>
        <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
      </div>
    </div>
  `;
  
  container.insertAdjacentHTML('beforeend', toastHTML);
  const toastEl = document.getElementById(toastId);
  if (typeof bootstrap !== 'undefined') {
    const bsToast = new bootstrap.Toast(toastEl, { delay: 3500 });
    bsToast.show();
    toastEl.addEventListener('hidden.bs.toast', function() {
      toastEl.remove();
    });
  } else {
    // Fallback if bootstrap object isn't available yet
    toastEl.style.display = 'block';
    setTimeout(() => toastEl.remove(), 3500);
  }
}

// Favorites Bookmarking Feature (Local Storage based)
function toggleFavorite(collegeId, event) {
  if (event) event.preventDefault();
  let favorites = JSON.parse(localStorage.getItem('fav_colleges') || '[]');
  const index = favorites.indexOf(collegeId);
  let isFav = false;
  
  if (index === -1) {
    favorites.push(collegeId);
    isFav = true;
    showToast('College added to bookmarks!', 'success');
  } else {
    favorites.splice(index, 1);
    showToast('College removed from bookmarks!', 'info');
  }
  
  localStorage.setItem('fav_colleges', JSON.stringify(favorites));
  
  // Update UI stars
  const stars = document.querySelectorAll(`.fav-star[data-id="${collegeId}"]`);
  stars.forEach(star => {
    if (isFav) {
      star.classList.add('active');
      star.innerHTML = '<i class="fas fa-star text-warning"></i>';
    } else {
      star.classList.remove('active');
      star.innerHTML = '<i class="far fa-star"></i>';
    }
  });
}

function loadFavorites() {
  const favorites = JSON.parse(localStorage.getItem('fav_colleges') || '[]');
  favorites.forEach(id => {
    const stars = document.querySelectorAll(`.fav-star[data-id="${id}"]`);
    stars.forEach(star => {
      star.classList.add('active');
      star.innerHTML = '<i class="fas fa-star text-warning"></i>';
    });
  });
}
