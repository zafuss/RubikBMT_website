$(document).ready(function() {
    function setupAutocomplete() {
        var $input = $('#keyword');
        var $results = $('#autocomplete-results');
        var currentIndex = -1;

        $input.on('input', function() {
            var query = $input.val();
            if (query.length < 2) {
                $results.hide();
                return;
            }

            var searchType = $("#searchType").val();
            var url = "/api/students/autocomplete";

            $.ajax({
                url: url,
                data: {
                    input: query,
                    type: searchType
                },
                success: function(data) {
                    if (data.length === 0) {
                        $results.hide();
                        return;
                    }

                    var suggestions = data.slice(0, 5).map(function(item) {
                        return '<div class="autocomplete-suggestion">' + item + '</div>';
                    }).join('');

                    $results.html(suggestions).show();
                    currentIndex = -1;
                }
            });
        });

        $results.on('click', '.autocomplete-suggestion', function() {
            $input.val($(this).text());
            $results.hide();
        });

        $(document).on('keydown', function(e) {
            if (e.key === 'ArrowDown') {
                e.preventDefault();
                navigateSuggestions(1);
            } else if (e.key === 'ArrowUp') {
                e.preventDefault();
                navigateSuggestions(-1);
            } else if (e.key === 'Enter') {
                e.preventDefault();
                selectSuggestion();
            }
        });

        function navigateSuggestions(direction) {
            var $suggestions = $results.find('.autocomplete-suggestion');
            if ($suggestions.length === 0) return;

            currentIndex = (currentIndex + direction + $suggestions.length) % $suggestions.length;
            $suggestions.removeClass('highlighted');
            $($suggestions.get(currentIndex)).addClass('highlighted');
        }

        function selectSuggestion() {
            var $highlighted = $results.find('.highlighted');
            if ($highlighted.length === 0) return;

            $input.val($highlighted.text());
            $results.hide();
        }

        $(document).on('click', function(event) {
            if (!$(event.target).closest('#keyword, #autocomplete-results').length) {
                $results.hide();
            }
        });
    }

    setupAutocomplete();
});
