var main = {
    init : function () {
        var _this = this;

        if($('#btn-read').length) {
            _this.read();
        }
        $('#btn-read').on('click', function () {
            _this.read();
        });
    },
    setMatchPage : function(msg) {
            var cdn = '/cdn/images/';
            var png = '.png';

            var container = document.getElementsByClassName('container')[0];

            for(var i=0;i<msg._embedded.matchDtoList.length;i++) {
                var matchGameId = $("#"+msg._embedded.matchDtoList[i].gameId);
                var owner = msg._embedded.matchDtoList[i].owner;

                var row = document.createElement('div');
                row.setAttribute('class', 'row bg-primary  mt-2 pt-5');
                var col = document.createElement('div');
                col.setAttribute('class', 'col-md-12');
                col.setAttribute('id', msg._embedded.matchDtoList[i].gameId);

                var championId = document.createElement('img');
                championId.setAttribute('src', cdn + '/champions/' + owner.championId + png);
                var spell1Id = document.createElement('img');
                spell1Id.setAttribute('src', cdn + '/spells/' + owner.spell1Id + png);
                var spell2Id = document.createElement('img');
                spell2Id.setAttribute('src', cdn + '/spells/' + owner.spell2Id + png);
                var item0 = document.createElement('img');
                item0.setAttribute('src', cdn + '/items/' + owner.item0 + png);
                var item1 = document.createElement('img');
                item1.setAttribute('src', cdn + '/items/' + owner.item1 + png);
                var item2 = document.createElement('img');
                item2.setAttribute('src', cdn + '/items/' + owner.item2 + png);
                var item3 = document.createElement('img');
                item3.setAttribute('src', cdn + '/items/' + owner.item3 + png);
                var item4 = document.createElement('img');
                item4.setAttribute('src', cdn + '/items/' + owner.item4 + png);
                var item5 = document.createElement('img');
                item5.setAttribute('src', cdn + '/items/' + owner.item5 + png);
                var perkPrimaryStyle = document.createElement('img');
                perkPrimaryStyle.setAttribute('src', cdn + '/runes/' + owner.perkPrimaryStyle + png);
                var perkSubStyle = document.createElement('img');
                perkSubStyle.setAttribute('src', cdn + '/rune_styles/' + owner.perkSubStyle + png);

                col.appendChild(championId);
                col.appendChild(spell1Id);
                col.appendChild(spell2Id);
                col.appendChild(item0);
                col.appendChild(item1);
                col.appendChild(item2);
                col.appendChild(item3);
                col.appendChild(item4);
                col.appendChild(item5);
                col.appendChild(perkPrimaryStyle);
                col.appendChild(perkSubStyle);
                row.appendChild(col);
                container.appendChild(row);
            }
            var nextPage = msg['_links']['next-page']['href'];
            apiUrl = baseUrl + '/' + nextPage.split("/").pop();
    },
    read : function () {
        var _this = this;
        $.ajax({
            type: 'GET',
            url: apiUrl,
            dataType: 'json',
            contentType:'application/hal+json; charset=utf-8'
        }).done(function(msg) {
            _this.setMatchPage(msg);
        }).fail(function (error) {
            if(error.status != 404)
                alert(JSON.stringify(error));
        });
    }
};
var baseUrl = '/api/matches';
var pathName = window.location.pathname.split("/");
var summonerName = pathName[pathName.length-2];
var apiUrl = baseUrl + '/' + summonerName;
main.init();